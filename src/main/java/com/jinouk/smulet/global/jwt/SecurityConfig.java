package com.jinouk.smulet.global.jwt;

import com.jinouk.smulet.global.exception.CustomExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final JWTUtil jwtUtil;
    private final CustomExceptionHandler authExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/" ,
                                "/login_page",
                                "/Register",
                                "/check_code",
                                "/send_code",
                                "/do_Register",
                                "/refreshT",
                                "/login",
                                "/jsCss/**",
                                "/images/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authExceptionHandler) // 401
                        .accessDeniedHandler(authExceptionHandler)      // 403
                )
                .addFilterBefore(new JWTAtuhFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
