package com.A105.prham.auth.filter;

import com.A105.prham.auth.service.SsoAuthService;
import com.A105.prham.auth.dto.response.RefreshTokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenRefreshFilter extends OncePerRequestFilter {

    private final SsoAuthService ssoAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().contains("/auth/sso/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);

        if (response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
            String refreshToken = request.getHeader("Refresh-Token");

            if (refreshToken != null) {
                try {
                    log.info("401 에러 감지, 새 토큰 발급");

                    RefreshTokenResponse newTokens = ssoAuthService.refreshToken(refreshToken);

                    response.setHeader("New-Access-Token", newTokens.getAccessToken());
                    response.setHeader("New-Refresh-Token", newTokens.getRefreshToken());
                    response.setHeader("Token-Expired", "true");

                    log.info("새 토큰 발급 완료");

                } catch (Exception e) {
                    log.error("토큰 갱신 실패: {}", e.getMessage());

                    // Refresh Token도 만료된 경우
                    response.setHeader("Refresh-Token-Expired", "true");
                    response.setHeader("Login-Required", "true");
                }
            }
        }
    }
}