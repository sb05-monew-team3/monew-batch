package com.monew.monew_batch.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.monew.monew_batch.entity.ArticleSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "chosun.api")
public class ChosunArticleProperties {
	private final String baseUrl;
	private final ArticleSource articleSource = ArticleSource.CHOSUN;
	private final List<String> feeds = List.of("politics", "economy", "national", "international", "culture-life",
		"opinion", "sports", "entertainments");
}