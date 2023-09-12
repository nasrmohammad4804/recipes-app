package com.nasr.recipesproject.security.filter;

import com.nasr.recipesproject.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.nasr.recipesproject.constant.ConstantField.ACCESS_TOKEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final List<String> uirShouldNotFiltered = List.of(
            "/api/login", "/sign-up"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            doFilter(request, response, filterChain);

        else {

            Cookie tokenCookie = null;


            for (Cookie cookie : cookies)
                if (cookie.getName().equals(ACCESS_TOKEN))
                    tokenCookie = cookie;

            try {

                if (tokenCookie != null) {
                    Authentication authentication = JwtUtil.validateToken(tokenCookie.getValue());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);

            } catch (Exception e) {

                log.error("error occurred : [{}] while decode token", e.getMessage());
                response.setStatus(SC_UNAUTHORIZED);
                response.getOutputStream().write("invalid token".getBytes());
            }
        }


    }


//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return uirShouldNotFiltered.contains(request.getServletPath());
//    }
}
