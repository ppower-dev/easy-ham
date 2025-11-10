package com.A105.prham.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtils {

    /**
     * JWT 토큰에서 사용자 ID(sub 클레임)를 추출하는 메서드
     * SSAFY JWT는 서명키를 모르므로 서명 검증 없이 payload만 디코딩
     */
    public String getUserIdFromToken(String token) {
        try {
            // JWT는 "헤더.페이로드.서명" 구조로 되어 있음
            String[] chunks = token.split("\\.");
            if (chunks.length != 3) {
                return null; // 올바른 JWT 형식이 아님
            }

            // Base64 URL 디코더로 페이로드 부분(chunks[1])을 디코딩
            java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));

            // JSON 파싱을 위한 ObjectMapper 생성
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var node = mapper.readTree(payload);

            // JWT의 "sub"(subject) 클레임에서 사용자 ID 추출
            return node.has("sub") ? node.get("sub").asText() : null;
        } catch (Exception e) {
            log.error("토큰에서 사용자 ID 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JWT 토큰의 만료 여부를 확인하는 메서드
     * exp(expiration) 클레임과 현재 시간을 비교
     */
    public boolean isTokenExpired(String token) {
        try {
            // JWT를 점(.)으로 분리
            String[] chunks = token.split("\\.");
            if (chunks.length != 3) {
                return true; // 잘못된 형식이면 만료로 간주
            }

            // 페이로드 부분을 Base64 디코딩
            java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));

            // JSON 파싱
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var node = mapper.readTree(payload);

            // "exp" 클레임이 있는지 확인
            if (node.has("exp")) {
                // exp는 Unix 타임스탬프 (초 단위)
                long exp = node.get("exp").asLong();
                // 현재 시간을 초 단위로 변환
                long currentTime = System.currentTimeMillis() / 1000;
                // 현재 시간이 만료 시간을 넘었으면 true
                return currentTime >= exp;
            }

            return true; // exp 클레임이 없으면 만료로 간주
        } catch (Exception e) {
            log.error("토큰 만료 확인 실패: {}", e.getMessage());
            return true; // 파싱 실패시 안전하게 만료로 간주
        }
    }
}