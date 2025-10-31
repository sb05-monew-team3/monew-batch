package com.monew.monew_batch.job.rss_article_collection.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.job.rss_article_collection.dto.HankyungArticleResponse;
import com.monew.monew_batch.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HankyungRssClient {

	private final RestClient hankyungRestClient;
	private final ArticleMapper articleMapper;

	public List<ArticleSaveDto> fetchArticles(String feed) {
		try {
			log.info("[한국경제] RSS 피드 요청 시작: feed={}", feed);

			HankyungArticleResponse response = hankyungRestClient.get()
				.uri("/" + feed)  // /feed/economy 형태
				.retrieve()
				.body(HankyungArticleResponse.class);

			if (response == null || response.getChannel() == null) {
				throw new RuntimeException("[한국경제] RSS 요청 실패 ");
			}

			if (response.getChannel().getItems() == null) {
				log.info("[한국경제] 기사가 존재하지 않음");
				return null;
			}

			List<HankyungArticleResponse.Item> items = response.getChannel().getItems();

			log.info("[한국경제] {}개의 기사", items.size());

			return items.stream()
				.map(articleMapper::toArticleSaveDto)
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("[한국경제]RSS 피드 호출 실패: feed={}", feed, e);
			throw new RuntimeException("[한국경제] RSS 피드 호출 실패: " + e.getMessage(), e);
		}
	}
}