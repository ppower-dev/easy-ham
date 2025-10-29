package com.A105.prham.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    //200 OK
//    BACKGROUND_GET_SUCCESS(200,HttpStatus.OK,"배경 목록이 로드되었습니다."),
    LOGIN_URL_TRANSFER(200,HttpStatus.OK,"로그인 URL 전송 성공"),
    LOGIN_SUCCESS(200,HttpStatus.OK,"로그인 성공"),
    REFRESH_SUCCESS(200,HttpStatus.OK,"토큰 새로고침 성공"),
    SIGNUP_SUCCESSED(200,HttpStatus.OK ,"회원가입 성공" ),
    BOOKMARK_SAVE_SUCCESS(200, HttpStatus.OK, "북마크 저장 성공"),
    ;


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
