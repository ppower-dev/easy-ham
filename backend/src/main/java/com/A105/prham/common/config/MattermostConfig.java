package com.A105.prham.common.config;

import java.util.ArrayList;
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
	// 수집할 채널 id 목록
	private List<String> allowedChannels = new ArrayList<>();

	//제외할 채널 목록
	private List<String> blockedChannels = new ArrayList<>();

	// 해당 채널이 수집 대상인지 확인
	public boolean isAllowedChannel(String channelId){
		//블록된 채널이면 거부
		if (blockedChannels.contains(channelId)) {
			return false;
		}

		// allowedChannels가 비어있으면 모든 채널 허용
		if (allowedChannels.isEmpty()){
			return true;
		}

		// allowedChannels에 포함된 경우에만 허용
		return allowedChannels.contains(channelId);
	}
}
