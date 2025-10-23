package com.monew.monew_batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.job.article.externalApi.naver.NaverApiProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

	private final NaverApiProperties naverApiProperties;

	@Bean
	public RestClient naverRestClient() {
		return RestClient.builder()
			.baseUrl(naverApiProperties.baseUrl())
			.defaultHeader("X-Naver-Client-Id", naverApiProperties.clientId())
			.defaultHeader("X-Naver-Client-Secret", naverApiProperties.clientSecret())
			.defaultHeader("Accept", "application/json")
			.defaultHeader("User-Agent", "Mozilla/5.0")
			.build();
	}
}
