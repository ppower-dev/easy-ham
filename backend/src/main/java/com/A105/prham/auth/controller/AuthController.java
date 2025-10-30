package com.A105.prham.auth.controller;

import com.A105.prham.auth.dto.request.RefreshTokenRequest;
import com.A105.prham.auth.dto.response.AccessTokenResponse;
import com.A105.prham.auth.dto.response.LoginResponse;
import com.A105.prham.auth.dto.response.RefreshTokenResponse;
import com.A105.prham.auth.dto.response.UserInfoResponse;
import com.A105.prham.auth.service.SsoAuthService;
import com.A105.prham.common.response.ApiResponseDto;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.common.response.SuccessCode;
import com.A105.prham.user.domain.User;
import com.A105.prham.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Value("${ssafy.sso.client-id}")
    private String clientId;

    @Value("${ssafy.sso.client-secret}")
    private String clientSecret;

    @Value("${ssafy.sso.redirect-uri}")
    private String redirectUri;

    private final SsoAuthService ssoAuthService;
    private final UserService userService;

    @GetMapping("/sso/login-url")
    public ApiResponseDto<String> getURL(){
        return ApiResponseDto.success(SuccessCode.LOGIN_URL_TRANSFER,"https://project.ssafy.com/oauth/sso-check");
    }

    @GetMapping("/sso/callback")
    public ApiResponseDto<LoginResponse> callback(@RequestParam("code") String code){
            try{
                // 1. 액세스 토큰 획득
                AccessTokenResponse tokenResponse = ssoAuthService.getAccessToken(code);
                // 2. 사용자 정보 조회
                UserInfoResponse userInfo = ssoAuthService.getUserInfo(tokenResponse.getAccessToken());

                // 이미 가입된 사용자인가? 분기 처리
                try{
                    User user = userService.findByEmail(userInfo.getEmail());
                    // 3. 응답 DTO 생성
                    LoginResponse loginResponse = LoginResponse.builder()
                            .token(tokenResponse)
                            .userId(user.getId())
                            .email(user.getEmail())
                            .name(user.getName())
                            .build();

                    return ApiResponseDto.success(SuccessCode.LOGIN_SUCCESS, loginResponse);
                }catch (Exception e){
                    //회원가입 필요
                    log.error(e.getMessage());
                    return ApiResponseDto.fail(ErrorCode.NOT_REGISTERED);
                }
            }catch (Exception e){
                return ApiResponseDto.fail((ErrorCode.INTERNAL_SERVER_ERROR));
            }
    }


    //테스트용
    @PostMapping("/refresh")
    public ApiResponseDto<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = ssoAuthService.refreshToken(request.getRefreshToken());
        return ApiResponseDto.success(SuccessCode.REFRESH_SUCCESS, response);
    }


}
