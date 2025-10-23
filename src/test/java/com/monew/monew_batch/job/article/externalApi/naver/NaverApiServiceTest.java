package com.monew.monew_batch.job.article.externalApi.naver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.monew.monew_batch.job.article.externalApi.naver.dto.NaverNewsResponse;

@SpringBootTest
@ActiveProfiles("test")
public class NaverApiServiceTest {

	@Autowired
	private NaverApiService naverApiService;

	@Test
	@DisplayName("네이버 api 데이터 한번 가져와 보기 ")
	public void getNewsTest() {
		NaverNewsResponse sim = naverApiService.getNews("윤석열", 10, 1, "sim");

		System.out.println("===== 네이버 뉴스 API 결과 =====");
		System.out.println("total: " + sim.getTotal());
		System.out.println("start: " + sim.getStart());
		System.out.println("display: " + sim.getDisplay());
		System.out.println("lastBuildDate: " + sim.getLastBuildDate());
		System.out.println("===============================");

		for (NaverNewsResponse.NewsItem item : sim.getItems()) {
			System.out.println("title: " + item.getTitle());
			System.out.println("description: " + item.getDescription());
			System.out.println("originallink: " + item.getOriginallink());
			System.out.println("link: " + item.getLink());
			System.out.println("pubDate: " + item.getPubDate());
			System.out.println("--------------------------------------");
		}
	}
}
