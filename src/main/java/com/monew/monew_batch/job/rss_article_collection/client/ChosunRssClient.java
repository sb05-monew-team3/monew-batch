package com.monew.monew_batch.job.rss_article_collection.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.job.rss_article_collection.dto.ChosunArticleResponse;
import com.monew.monew_batch.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChosunRssClient {

	private final RestClient chosunRestClient;
	private final ArticleMapper articleMapper;

	public List<ArticleSaveDto> fetchArticles(String feed) {
		try {
			log.info("[조선뉴스] RSS 피드 요청 시작: feed={}", feed);

			ChosunArticleResponse response = chosunRestClient.get()
				.uri(uriBuilder -> uriBuilder
					.path("/" + feed + "/")
					.queryParam("outputType", "xml")
					.build(false))
				.retrieve()
				.body(ChosunArticleResponse.class);

			if (response == null || response.getChannel() == null) {
				throw new RuntimeException("[조선뉴스] RSS 요청 실패 ");
			}

			if (response.getChannel().getItems() == null) {
				log.info("[조선뉴스] 기사가 존재하지 않음");
				return null;
			}

			List<ChosunArticleResponse.Item> items = response.getChannel().getItems();

			log.info("[조선뉴스] {}개의 기사", items.size());

			return items.stream()
				.map(articleMapper::toArticleSaveDto)
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("[조선뉴스]RSS 피드 호출 실패: feed={}", feed, e);
			throw new RuntimeException("[조선뉴스] RSS 피드 호출 실패: " + e.getMessage(), e);
		}
	}
}
