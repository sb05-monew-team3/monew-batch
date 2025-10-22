package com.monew.monew_batch.job.article.externalApi.naver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.monew.monew_batch.job.article.externalApi.naver.dto.NaverNewsResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverApiService {

	private final RestTemplate restTemplate;
	private final NaverApiProperties naverApiProperties;

	public NaverNewsResponse getNews(String queryDecodedOrEncoded, int display, int start, String sort) {
		String normalized = URLDecoder.decode(queryDecodedOrEncoded, StandardCharsets.UTF_8);

		String url = UriComponentsBuilder.fromUriString(naverApiProperties.baseUrl())
			.queryParam("query", normalized)
			.queryParam("display", display)
			.queryParam("start", start)
			.queryParam("sort", sort)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUriString();
		log.debug("Request URL: {}", url);

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", naverApiProperties.clientId());
		headers.set("X-Naver-Client-Secret", naverApiProperties.clientSecret());

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<NaverNewsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
				NaverNewsResponse.class);

			log.debug("Response Status: {}", response.getBody());
			return response.getBody();
		} catch (Exception e) {
			log.error("네이버 뉴스 API 호출 실패 ", e);
			throw new RuntimeException("네이버 뉴스 API 호출 실패: " + e.getMessage());
		}
	}
}

