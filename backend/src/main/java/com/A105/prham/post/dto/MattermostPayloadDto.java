package com.A105.prham.post.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MattermostPayloadDto(
	@JsonProperty("team_id")
	String teamId,

	@JsonProperty("channel_id")
	String channelId,

	@JsonProperty("user_id")
	String userId,

	@JsonProperty("user_name")
	String userName,

	@JsonProperty("post_id")
	String postId,

	@JsonProperty("text")
	String text,

	@JsonProperty("file_ids")
	List<String> fileIds,

	//mm은 메시지 생성 일시 항목을 "create_at"으로 준다...
	//헷갈리지 말기!!
	@JsonProperty("create_at")
	Long createAt
) {
}
