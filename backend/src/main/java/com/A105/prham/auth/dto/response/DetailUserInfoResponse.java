package com.A105.prham.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DetailUserInfoResponse {
    private String email;
    private String name;
    private String edu;
    private String entRegn;
}