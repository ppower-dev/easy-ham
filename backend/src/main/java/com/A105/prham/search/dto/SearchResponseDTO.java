package com.A105.prham.search.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {

    private String postId;
    private String channelId;
    private String userId;
    private String originalText;
    private String cleanedText;
    private String deadline;
    private String createdAt;

    // MessageDocument에서 변환
    public static SearchResponseDTO from(MessageDocument document) {
        return new SearchResponseDTO(
                document.getPostId(),
                document.getChannelId(),
                document.getUserId(),
                document.getOriginalText(),
                document.getCleanedText(),
                document.getDeadline(),
                document.getCreatedAt()
        );
    }
}