package com.nasr.recipesproject.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasr.recipesproject.constant.ConstantField;
import com.nasr.recipesproject.enumeration.TokenType;
import com.nasr.recipesproject.util.JwtUtil;
import com.nasr.recipesproject.util.TimeUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.nasr.recipesproject.constant.ConstantField.getSigningKey;
import static com.nasr.recipesproject.enumeration.TokenType.*;
import static com.nasr.recipesproject.util.TimeUtility.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public void setPostOnly(boolean postOnly) {
        super.setPostOnly(postOnly);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String userName = request.getParameter("email");
        String password = request.getParameter("password");
        log.info("email is : {}  and password is : {}", userName, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String email =  authentication.getName();

        Algorithm algorithm = Algorithm.HMAC256(getSigningKey().getBytes());
        String accessToken = JwtUtil.generateToken(email,request,algorithm, TokenType.ACCESS_TOKEN);


        String refreshToken = JwtUtil.generateToken(email,request,algorithm,TokenType.REFRESH_TOKEN);

        Map<String, String> data = new HashMap<>();
        data.put("expiredAt", String.valueOf(convertMiliSecondToSecond(ConstantField.ACCESS_TOKEN_EXPIRATION_TIME)));

        JwtUtil.convertTokensToCookie(new String[] {accessToken,refreshToken},response);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Allow-Origin","http://127.0.0.1:5500");
        response.setHeader("Access-Control-Allow-Credentials","true");

        new ObjectMapper().writeValue(response.getOutputStream(),data);
    }

}
