package com.A105.prham.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@JsonDeserialize(builder = AccessTokenResponse.AccessTokenResponseBuilder.class)
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

//    @JsonProperty("token_type")
//    private String tokenType;

//    @JsonProperty("expires_in")
//    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

//    private String scope;
}