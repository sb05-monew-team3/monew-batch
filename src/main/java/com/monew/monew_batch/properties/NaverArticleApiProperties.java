package com.monew.monew_batch.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.monew.monew_batch.entity.ArticleSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "naver.api")
@Getter
@RequiredArgsConstructor
public class NaverArticleApiProperties {
	private final String clientId;
	private final String clientSecret;
	private final String baseUrl;
	private final ArticleSource articleSource = ArticleSource.NAVER;
}