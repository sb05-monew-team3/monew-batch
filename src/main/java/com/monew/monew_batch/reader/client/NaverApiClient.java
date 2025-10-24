package com.monew.monew_batch.reader.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.mapper.ArticleMapper;
import com.monew.monew_batch.reader.dto.NaverArticleResponse;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverApiClient {

	private final ArticleMapper articleMapper;
	private final RestClient naverRestClient;

	/**
	 *
	 * @param query : 키워드
	 * @param display : 한 페이지 수
	 * @param start : 페이지 수
	 * @param sort : 정렬 기준 "sim: 정확도 내림차", "date: 날짜순 내림차 "
	 * @return
	 */
	public List<ArticleSaveDto> fetchArticles(String query, int display, int start, String sort) {
		try {
			NaverArticleResponse body = naverRestClient.get()
				.uri(uriBuilder -> uriBuilder
					.queryParam("query", query)
					.queryParam("display", display)
					.queryParam("start", start)
					.queryParam("sort", sort)
					.build(false)
				)
				.retrieve()
				.body(NaverArticleResponse.class);

			List<ArticleSaveDto> dtos = new ArrayList<>();

			if (body == null || body.getItems() == null)
				return dtos;

			body.getItems().forEach(item -> {
				ArticleSaveDto articleSaveDto = articleMapper.toArticleSaveDto(item);
				dtos.add(articleSaveDto);
			});

			return dtos;
		} catch (Exception e) {
			log.error("네이버 뉴스 API 호출 실패", e);
			throw new RuntimeException("네이버 뉴스 API 호출 실패: " + e.getMessage(), e);
		}
	}

}
