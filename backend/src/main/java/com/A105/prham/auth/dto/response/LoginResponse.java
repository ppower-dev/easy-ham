package com.A105.prham.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LoginResponse {
    private AccessTokenResponse token;
    private String name;
    private String email;
    private Long userId;
}