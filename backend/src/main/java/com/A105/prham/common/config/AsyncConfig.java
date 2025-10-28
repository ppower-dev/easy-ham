package com.A105.prham.common.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	//mm 메시지 비동기로 가져옴

	@Bean(name = "webhookTaskExecutor")
	public Executor webhookTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);   // 기본 스레드 수
		executor.setMaxPoolSize(10);   // 최대 스레드 수
		executor.setQueueCapacity(50); // 대기 큐
		executor.setThreadNamePrefix("webhook-"); // 스레드 이름 접두사
		executor.initialize();
		return executor;
	}
}
