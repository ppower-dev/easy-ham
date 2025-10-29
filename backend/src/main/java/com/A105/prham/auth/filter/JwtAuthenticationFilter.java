package com.A105.prham.auth.filter;

import com.A105.prham.auth.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    /**
     * 모든 HTTP 요청을 가로채서 JWT 토큰을 검증하고 인증 정보를 설정하는 메서드
     * OncePerRequestFilter를 상속받아 요청당 한 번만 실행됨
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰 추출
        String token = extractToken(request);

        // 토큰이 존재하고 만료되지 않았다면
        if (token != null && !jwtUtils.isTokenExpired(token)) {
            // 토큰에서 사용자 ID 추출
            String userId = jwtUtils.getUserIdFromToken(token);

            if (userId != null) {
                // Spring Security의 Authentication 객체 생성
                // 첫 번째 파라미터: principal (사용자 식별자)
                // 두 번째 파라미터: credentials (비밀번호, JWT에서는 null)
                // 세 번째 파라미터: authorities (권한 목록, 현재는 빈 리스트)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

                // 요청에 대한 세부 정보 설정 (IP 주소, 세션 ID 등)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보 저장
                // 이후 컨트롤러에서 @AuthenticationPrincipal로 접근 가능
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청 전달 (필터 체인 계속 진행)
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출하는 메서드
     * Authorization: Bearer {token} 형식에서 토큰 부분만 추출
     */
    private String extractToken(HttpServletRequest request) {
        // Authorization 헤더 값 가져오기
        String bearerToken = request.getHeader("Authorization");

        // "Bearer "로 시작하는지 확인
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // "Bearer " 이후의 토큰 부분만 반환 (7글자 이후)
            return bearerToken.substring(7);
        }
        return null; // 토큰이 없거나 형식이 잘못됨
    }
}