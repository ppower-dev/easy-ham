package com.A105.prham.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/webhook/**").permitAll()  // webhook 경로는 인증 없이 허용
                        .requestMatchers("/api/**").permitAll()          // 모든 API 허용
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (API용)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .httpBasic(httpBasic -> httpBasic.disable())  // HTTP Basic 인증 비활성화
                .formLogin(formLogin -> formLogin.disable());  // 폼 로그인 비활성화

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");  // 모든 origin 허용
        configuration.addAllowedMethod("*");         // 모든 HTTP 메소드 허용
        configuration.addAllowedHeader("*");         // 모든 헤더 허용
        configuration.setAllowCredentials(true);     // 인증 정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // /api/** 경로에 CORS 적용

        return source;
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedPeriod(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedLineFeed(true);  //  줄바꿈 허용
        firewall.setAllowUrlEncodedCarriageReturn(true);  // 캐리지 리턴 허용
        return firewall;
    }
}