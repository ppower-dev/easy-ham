package com.A105.prham.user.controller;

import com.A105.prham.common.response.ApiResponseDto;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.common.response.SuccessCode;
import com.A105.prham.user.dto.request.UserSignupRequest;
import com.A105.prham.user.entity.User;
import com.A105.prham.user.entity.UserPosition;
import com.A105.prham.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public ApiResponseDto<User> createUser(@RequestBody UserSignupRequest request) {
        try{
            User user = userService.createUser(request);
            return ApiResponseDto.success(SuccessCode.SIGNUP_SUCCESSED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponseDto.fail(ErrorCode.BAD_REQUEST);
        }

    }

}

