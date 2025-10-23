package com.monew.monew_batch.job.article.externalApi.naver;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "naver.api")
@Getter
@RequiredArgsConstructor
public class NaverApiProperties {
	private final String clientId;
	private final String clientSecret;
	private final String baseUrl;
}