package com.A105.prham.user_notice_like.controller;

import com.A105.prham.common.annotation.UserId;
import com.A105.prham.common.response.ApiResponseDto;
import com.A105.prham.common.response.SuccessCode;
import com.A105.prham.user_notice_like.service.UserNoticeLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class UserNoticeLikeController {

    private final UserNoticeLikeService userNoticeLikeService;

    @PostMapping("/{noticeId}")
    public ApiResponseDto addBookmark(@UserId(required = true) Long userId, @PathVariable Long noticeId) {
        return ApiResponseDto.success(SuccessCode.BOOKMARK_SAVE_SUCCESS, userNoticeLikeService.saveBookmarks(userId, noticeId));
    }

    @DeleteMapping("/{noticeId}")
    public ApiResponseDto deleteBookmark(@UserId(required = true) Long userId, @PathVariable Long noticeId) {
        userNoticeLikeService.deleteBookmarks(userId, noticeId);
        return ApiResponseDto.success(SuccessCode.BOOKMARK_DELETE_SUCCESS);
    }
}
