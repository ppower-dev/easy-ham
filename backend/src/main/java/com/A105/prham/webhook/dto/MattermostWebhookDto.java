package com.A105.prham.webhook.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MattermostWebhookDto(
	@JsonProperty("token") String token,
	@JsonProperty("team_id") String teamId,
	@JsonProperty("team_domain") String teamDomain,
	@JsonProperty("channel_id") String channelId,
	@JsonProperty("channel_name") String channelName,
	@JsonProperty("timestamp") Long timestamp,
	@JsonProperty("user_id") String userId,
	@JsonProperty("user_name") String userName,
	@JsonProperty("post_id") String postId,
	@JsonProperty("text") String text,
	@JsonProperty("trigger_word") String triggerWord,
	@JsonProperty("file_ids") List<String> fileIds
) {
	// emoji 제거
	public String getCleanedText() {
		if(text == null) return "";

		// 1. :emoji_name: 형태 제거
		String cleaned = text.replaceAll(":[a-zA-Z0-9_+-]+:", "");

		// 2. Unicode 이모지 제거
		cleaned = cleaned.replaceAll("[\\p{So}\\p{Sk}]", "");

		// 3. 연속 공백 정리
		cleaned = cleaned.replaceAll("\\s+", " ").trim();

		return cleaned;
	}
}

