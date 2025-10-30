package com.A105.prham.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserInfoResponse {
    private String userId;
    private String email;
    private String name;
}