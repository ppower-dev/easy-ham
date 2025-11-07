package com.A105.prham.common.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {

	@Bean
	public CacheManager cacheManager10Min() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		List<CaffeineCache> caches = List.of(
			new CaffeineCache("mattermostUser", Caffeine.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.maximumSize(1000)
				.build()),
			new CaffeineCache("mattermostChannels", Caffeine.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build())
		);
		cacheManager.setCaches(caches);
		return cacheManager;
	}
}
