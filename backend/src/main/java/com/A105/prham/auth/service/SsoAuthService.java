package com.A105.prham.auth.service;

import com.A105.prham.auth.dto.response.AccessTokenResponse;
import com.A105.prham.auth.dto.response.DetailUserInfoResponse;
import com.A105.prham.auth.dto.response.RefreshTokenResponse;
import com.A105.prham.auth.dto.response.UserInfoResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SsoAuthService {

    @Value("${ssafy.sso.client-id}")
    private String clientId;

    @Value("${ssafy.sso.client-secret}")
    private String clientSecret;

    @Value("${ssafy.sso.redirect-uri}")
    private String redirectUri;

    @Value("${ssafy.sso.token-request-url}")
    private String tokenUrl;

    @Value("${ssafy.sso.user-info-url}")
    private String userInfoUrl;

    @Value("${ssafy.openAPI.key}")
    private String ssafyOpenAPIKey;
    @Value("${ssafy.openAPI.user-detail-url}")
    private String userDetailUrl;

    private final RestTemplate restTemplate;

    public AccessTokenResponse getAccessToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders() ;
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

            String bodyStr = response.getBody();
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("토큰 요청 실패: " + response.getStatusCode() + " / " + preview(bodyStr));
            }
            if (bodyStr == null || bodyStr.isBlank()) {
                throw new IllegalStateException("토큰 응답이 비어 있습니다.");
            }

            ObjectMapper om = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            AccessTokenResponse tokenResponse = om.readValue(bodyStr, AccessTokenResponse.class);

            return tokenResponse;

        } catch (Exception e) {
            log.error("토큰 요청 실패: {}", e.getMessage());
            throw new RuntimeException("SSO 토큰 획득 실패", e);
        }
    }

    private String preview(String s) {
        int max = 400;
        return s == null ? "null" : (s.length() > max ? s.substring(0, max) + "..." : s);
    }

    public DetailUserInfoResponse getDetailUserInfo(String accessToken, String userId) {
        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<?> request = new HttpEntity<>(headers);

            // URL 템플릿 구성
            String url = String.format("%s/%s?apiKey=%s", userDetailUrl, userId, ssafyOpenAPIKey);

            // GET 요청 전송
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            if (body == null) {
                throw new IllegalStateException("응답 본문이 비어 있습니다.");
            }

            // 응답 매핑
            return DetailUserInfoResponse.builder()
                    .edu((String) body.get("edu"))
                    .email((String) body.get("email"))
                    .name((String) body.get("name"))
                    .entRegn((String) body.get("entRedgn"))
                    .build();

        } catch (Exception e) {
            log.error("사용자 상세 정보 조회 실패: {}", e.getMessage(), e);
            throw new RuntimeException("SSAFY 사용자 상세 정보 조회 실패", e);
        }
    }


    public UserInfoResponse getUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoUrl, HttpMethod.GET, request, Map.class);

            Map<String, Object> body = response.getBody();

            return UserInfoResponse.builder()
                    .userId((String) body.get("userId"))
                    .email((String) body.get("email"))
                    .name((String) body.get("name"))
                    .build();
        } catch (Exception e) {
            log.error("사용자 정보 조회 실패: {}", e.getMessage());
            throw new RuntimeException("사용자 정보 조회 실패", e);
        }
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("refresh_token", refreshToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, request, String.class);

            ObjectMapper om = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return om.readValue(response.getBody(), RefreshTokenResponse.class);
        } catch (Exception e) {
            log.error("토큰 갱신 실패: {}", e.getMessage());
            throw new RuntimeException("토큰 갱신 실패", e);
        }
    }
}