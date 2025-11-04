package com.A105.prham.notification.controller;

import com.A105.prham.common.response.ApiResponseDto;
import com.A105.prham.common.response.SuccessCode;
import com.A105.prham.notification.dto.request.KeywordCreateRequest;
import com.A105.prham.notification.service.NotificationService;
import com.A105.prham.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/keywords")
    public ApiResponseDto addKeyword(@AuthenticationPrincipal User user, @RequestBody KeywordCreateRequest keywordCreateRequest) {
        notificationService.addKeyword(user, keywordCreateRequest);
        return ApiResponseDto.success(SuccessCode.KEYWORD_ADD_SUCCESS);
    }
}
