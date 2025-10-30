package com.A105.prham.user_notice_like.dto;

import lombok.Builder;

@Builder
public record UserNoticeLikeDeleteResponse(
        Boolean isLiked
) {
}
