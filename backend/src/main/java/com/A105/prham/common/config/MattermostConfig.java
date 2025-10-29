package com.A105.prham.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mattermost.webhook")
public class MattermostConfig {

	private String allowedChannels;
	private String blockedChannels;

	// List로 변환하는 메서드
	public List<String> getAllowedChannelList() {
		if (allowedChannels == null || allowedChannels.isBlank()) {
			return new ArrayList<>();
		}
		return Arrays.asList(allowedChannels.split(","));
	}

	public List<String> getBlockedChannelList() {
		if (blockedChannels == null || blockedChannels.isBlank()) {
			return new ArrayList<>();
		}
		return Arrays.asList(blockedChannels.split(","));
	}

	// 해당 채널이 수집 대상인지 확인
	public boolean isAllowedChannel(String channelId) {
		List<String> blocked = getBlockedChannelList();
		List<String> allowed = getAllowedChannelList();

		// 블록된 채널이면 거부
		if (blocked.contains(channelId)) {
			return false;
		}

		// allowedChannels가 비어있으면 모든 채널 허용
		if (allowed.isEmpty()) {
			return true;
		}

		// allowedChannels에 포함된 경우에만 허용
		return allowed.contains(channelId);
	}
}