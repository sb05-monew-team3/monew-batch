package com.monew.monew_batch.job.article.externalApi.naver;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.job.article.externalApi.naver.dto.NaverNewsResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverApiService {

	private final RestClient naverRestClient;

	public NaverNewsResponse getNews(String query, int display, int start, String sort) {
		try {
			return naverRestClient.get()
				.uri(uriBuilder -> uriBuilder
					.queryParam("query", query)
					.queryParam("display", display)
					.queryParam("start", start)
					.queryParam("sort", sort)
					.build(false)
				)
				.retrieve()
				.body(NaverNewsResponse.class);

		} catch (Exception e) {
			log.error("네이버 뉴스 API 호출 실패", e);
			throw new RuntimeException("네이버 뉴스 API 호출 실패: " + e.getMessage(), e);
		}
	}
}

