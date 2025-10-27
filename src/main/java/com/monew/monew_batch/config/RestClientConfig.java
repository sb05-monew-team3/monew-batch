package com.monew.monew_batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.properties.NaverArticleApiProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

	private final NaverArticleApiProperties naverArticleApiProperties;

	@Bean
	public RestClient naverRestClient() {
		return RestClient.builder()
			.baseUrl(naverArticleApiProperties.getBaseUrl())
			.defaultHeader("X-Naver-Client-Id", naverArticleApiProperties.getClientId())
			.defaultHeader("X-Naver-Client-Secret", naverArticleApiProperties.getClientSecret())
			.defaultHeader("Accept", "application/json")
			.defaultHeader("User-Agent", "Mozilla/5.0")
			.build();
	}
}
