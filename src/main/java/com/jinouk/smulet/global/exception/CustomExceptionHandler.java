package com.jinouk.smulet.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomExceptionHandler implements AuthenticationEntryPoint , AccessDeniedHandler
{
    // 인증 안됨 (401)
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
    }

    // 권한 없음 (403)
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException
    {

        sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
    }

    private void sendJsonError(HttpServletResponse response, int statusCode, String message) throws IOException
    {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> error = new HashMap<>();
        error.put("error", message);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(error);

        response.getWriter().write(json);
    }
}
