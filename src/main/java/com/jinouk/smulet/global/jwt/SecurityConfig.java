package com.jinouk.smulet.global.jwt;

import com.jinouk.smulet.global.exception.CustomExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final CustomExceptionHandler authExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 설정 추가
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login_page",
                                "/Register",
                                "/check_code",
                                "/send_code",
                                "/do_Register",
                                "/refreshT",
                                "/login",
                                "/jsCss/**",
                                "/images/**",
                                "/techtree/**",
                                "/course",
                                "/getTC",
                                "/deleteTC"
                        ).permitAll()
                        .requestMatchers(
                                "/member/delete",
                                "/check_pw_button",
                                "/my_page",
                                "/techtree",
                                "/PWupdate_button",
                                "/update",
                                "/userinfo",
                                "/tech_tree",
                                "/my_page1",
                                "/a",
                                "/addTimetable",
                                "/addTC",
                                "/tableId_List",
                                "/detail",
                                "/representative_Timetable",
                                "/getTS2TE"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authExceptionHandler)
                        .accessDeniedHandler(authExceptionHandler)
                )
                .addFilterBefore(new JWTAtuhFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ✅ CORS 설정 Bean 등록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000")); // ✅ 프론트 포트
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 쿠키 포함 여부 (HttpOnly 쿠키 사용 시 필요)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}