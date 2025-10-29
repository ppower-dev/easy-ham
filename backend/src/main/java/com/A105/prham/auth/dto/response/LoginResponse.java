package com.A105.prham.auth.dto.response;

import com.A105.prham.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
public class LoginResponse {
    private AccessTokenResponse token;
    private String name;
    private String email;
    private Long userId;
}