package com.A105.prham.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    //200 OK
//    BACKGROUND_GET_SUCCESS(200,HttpStatus.OK,"배경 목록이 로드되었습니다."),
    LOGIN_SUCCESS(200,HttpStatus.OK,"로그인 성공")
    //201 CREATED
//    ROOM_CREATE_SUCCESS(201, HttpStatus.CREATED, "방이 생성되었습니다."),
    ;


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
