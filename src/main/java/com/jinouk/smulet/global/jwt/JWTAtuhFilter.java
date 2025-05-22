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
        System.out.println(AuthHeader);
        if(AuthHeader != null && AuthHeader.startsWith("Bearer "))
        {
            String token = AuthHeader.substring(7);
            try {
                if(jwtUtil.validateToken(token)){
                    String username = jwtUtil.getUserName(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e)
            {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");

                Map<String, String> error = new HashMap<>();
                error.put("error", "토큰이 만료되었거나 유효하지 않습니다");

                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(error);

                response.getWriter().write(json);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
