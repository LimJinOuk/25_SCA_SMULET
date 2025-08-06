package com.jinouk.smulet.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JWTAtuhFilter extends OncePerRequestFilter
{
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String AuthHeader = request.getHeader("Authorization");
        if(AuthHeader != null && AuthHeader.startsWith("Bearer "))
        {
            String token = AuthHeader.substring(7);
            System.out.println("JWT 토큰: " + token);
            try {
                boolean valid = jwtUtil.validateToken(token);
                System.out.println("토큰 유효성 검사 결과: " + valid);
                if(valid){
                    String username = jwtUtil.getUserName(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e)
            {
                System.out.println("JWT 유효성 검사 실패: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
