package com.monew.monew_batch.job.article.externalApi.naver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "naver.api")
public record NaverApiProperties(String clientId, String clientSecret, String baseUrl) {
}
