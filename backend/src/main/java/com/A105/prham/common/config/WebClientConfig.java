package com.A105.prham.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Value;

@Configuration
public class WebClientConfig {

	@Value("${mattermost.api.url}")
	private String mmApiUrl;

	@Value("${mattermost.api.admin-token}")
	private String adminToken;

	@Bean
	public WebClient mattermostApiWebClient(WebClient.Builder builder) {
		return builder
			.baseUrl(mmApiUrl)
			.defaultHeader("Authorization", "Bearer" + adminToken)
			.build();
	}
}
