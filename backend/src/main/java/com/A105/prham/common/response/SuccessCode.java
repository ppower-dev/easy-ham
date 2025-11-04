package com.A105.prham.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    //200 OK
    LOGIN_SUCCESS(200,HttpStatus.OK,"로그인 성공"),
    WEBHOOK_RECEIVED(200, HttpStatus.OK, "웹훅이 정상적으로 수신되었습니다."),
    LOGIN_URL_TRANSFER(200,HttpStatus.OK,"로그인 URL 전송 성공"),
    REFRESH_SUCCESS(200,HttpStatus.OK,"토큰 새로고침 성공"),
    SIGNUP_SUCCESSED(200,HttpStatus.OK ,"회원가입 성공" ),
    BOOKMARK_SAVE_SUCCESS(200, HttpStatus.OK, "북마크 저장 성공"),
    BOOKMARK_DELETE_SUCCESS(200, HttpStatus.OK, "북마크 해제 성공"),
    BOOKMARK_GET_SUCCESS(200, HttpStatus.OK, "북마크 목록 조회 성공"),
    SUCCESS(200,HttpStatus.OK, "요청 성공"),
    NOT_REGISTERED(403, HttpStatus.FORBIDDEN,"회원가입 후 이용해 주세요"),
    //201 CREATED
    POST_CREATED(201, HttpStatus.CREATED, "메시지가 저장되었습니다.");


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
