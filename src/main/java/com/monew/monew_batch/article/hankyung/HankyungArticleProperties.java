package com.monew.monew_batch.article.hankyung;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.monew.monew_batch.entity.ArticleSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "hankyung.api")
public class HankyungArticleProperties {
	private final String baseUrl;
	private final ArticleSource articleSource = ArticleSource.NAVER;
	private final List<String> feeds = List.of("economy", "it", "international", "life", "sports", "video", "finance",
		"realestate", "politics", "society", "opinion", "entertainment");
}
