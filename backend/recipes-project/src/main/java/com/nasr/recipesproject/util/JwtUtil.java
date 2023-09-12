package com.nasr.recipesproject.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nasr.recipesproject.constant.ConstantField;
import com.nasr.recipesproject.enumeration.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nasr.recipesproject.constant.ConstantField.*;
import static com.nasr.recipesproject.util.TimeUtility.convertMiliSecondToSecond;

public class JwtUtil {

    public static String generateToken(String username, HttpServletRequest request, Algorithm algorithm, TokenType tokenType){

        Date date;

        if(tokenType.equals(TokenType.ACCESS_TOKEN))
            date = new Date(System.currentTimeMillis() + ConstantField.ACCESS_TOKEN_EXPIRATION_TIME);

        else date= new Date(System.currentTimeMillis() + ConstantField.REFRESH_TOKEN_EXPIRATION_TIME);

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(date)
                .withIssuer(request.getRequestURI())
                .sign(algorithm);
    }

    public static Authentication validateToken(String token){
        Pattern pattern = Pattern.compile("\\b(\\S+)\\.(\\S+)\\.(\\S+)");
        Matcher matcher = pattern.matcher(token);

        if (matcher.find())
            token = matcher.group();

        Algorithm algorithm = Algorithm.HMAC256(getSigningKey().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        return new UsernamePasswordAuthenticationToken(username, null, null);

    }

    public static void convertTokensToCookie(String[] tokens, HttpServletResponse response){
        String accessToken = tokens[0];
        String refreshToken = tokens[1];

        Cookie accessTokenCookie = new Cookie(ConstantField.ACCESS_TOKEN, accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setAttribute("SameSite","none");
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge(convertMiliSecondToSecond(ACCESS_TOKEN_EXPIRATION_TIME).intValue());

        Cookie refreshTokenCookie = new Cookie(ConstantField.REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite","none");
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(convertMiliSecondToSecond(REFRESH_TOKEN_EXPIRATION_TIME).intValue());

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
