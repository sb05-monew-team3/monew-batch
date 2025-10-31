package com.monew.monew_batch.job.api_article_collection.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.job.api_article_collection.dto.NaverArticleResponse;
import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverApiClient {

	private final RestClient naverRestClient;
	private final ArticleMapper articleMapper;

	public List<ArticleSaveDto> fetchArticles(String query, int display, int start, String sort) {
		try {
			NaverArticleResponse naverArticleResponse = naverRestClient.get()
				.uri(uriBuilder -> uriBuilder
					.queryParam("query", query)
					.queryParam("display", display)
					.queryParam("start", start)
					.queryParam("sort", sort)
					.build(false)
				)
				.retrieve()
				.body(NaverArticleResponse.class);

			if (naverArticleResponse == null) {
				return null;
			}

			List<NaverArticleResponse.ArticleItem> items = naverArticleResponse.getItems();

			List<ArticleSaveDto> returnValue = new ArrayList<>();
			if (items == null || items.isEmpty()) {
				return null;
			} else {
				for (NaverArticleResponse.ArticleItem item : items) {
					ArticleSaveDto articleSaveDto = articleMapper.toArticleSaveDto(item);
					returnValue.add(articleSaveDto);
				}
			}
			return returnValue;
		} catch (Exception e) {
			log.error("네이버 뉴스 API 호출 실패", e);
			throw new RuntimeException("네이버 뉴스 API 호출 실패: " + e.getMessage(), e);
		}
	}

}
