package com.A105.prham.common.annotation;

import com.A105.prham.auth.dto.response.UserInfoResponse;
import com.A105.prham.auth.service.SsoAuthService;
import com.A105.prham.auth.util.JwtUtils;
import com.A105.prham.common.exception.CustomException;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.user.domain.User;
import com.A105.prham.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final SsoAuthService ssoAuthService;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserId.class) != null &&
                (parameter.getParameterType().equals(Long.class) ||
                        parameter.getParameterType().equals(long.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        UserId annotation = parameter.getParameterAnnotation(UserId.class);

        try {
            // 1. Header에서 토큰 추출
            String token = extractTokenFromHeader(request);

            if (token == null || token.trim().isEmpty()) {
                if (annotation.required()) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
                return null;
            }

            // 2. 기존 JwtUtils 메소드 사용해서 userId 추출
            UserInfoResponse userInfo = ssoAuthService.getUserInfo(token);

            if (userInfo == null || userInfo.getEmail() == null) {
                if (annotation.required()) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
                return null;
            }

            // 3. 이메일로 우리 DB에서 사용자 찾기/생성
            User user = userRepository.findByEmail(userInfo.getEmail())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            Long userId = user.getId();

            log.debug("SSO 사용자 {}({})의 userId: {}",
                    userInfo.getName(), userInfo.getEmail(), userId);
            return userId;

        } catch (Exception e) {
            log.error("토큰에서 유저 아이디 추출 실패", e);

            if (annotation.required()) {
                throw new CustomException(ErrorCode.NOT_FOUND);
            }
            return null;
        }
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        // Authorization Header에서 Bearer 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " 제거
        }

        return null;
    }
}
