package com.monew.monew_batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.article.naver.NaverApiProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

	private final NaverApiProperties naverApiProperties;

	@Bean
	public RestClient naverRestClient() {
		return RestClient.builder()
			.baseUrl(naverApiProperties.getBaseUrl())
			.defaultHeader("X-Naver-Client-Id", naverApiProperties.getClientId())
			.defaultHeader("X-Naver-Client-Secret", naverApiProperties.getClientSecret())
			.defaultHeader("Accept", "application/json")
			.defaultHeader("User-Agent", "Mozilla/5.0")
			.build();
	}
}
