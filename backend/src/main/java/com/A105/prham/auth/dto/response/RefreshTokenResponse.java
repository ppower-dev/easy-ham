package com.A105.prham.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;


    @JsonProperty("refresh_token")
    private String refreshToken;

//    @JsonProperty("refresh_token_expires_in")
//    private String refreshTokenExpiresIn;
//    @JsonProperty("token_type")
//    private String tokenType;

//    @JsonProperty("expires_in")
//    private String expiresIn;

}