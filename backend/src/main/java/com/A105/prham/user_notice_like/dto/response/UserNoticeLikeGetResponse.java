package com.A105.prham.user_notice_like.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserNoticeLikeGetResponse(
        List<UserNoticeLikeDto> notices
) {
}
