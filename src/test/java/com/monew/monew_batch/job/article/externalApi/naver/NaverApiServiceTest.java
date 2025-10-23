package com.monew.monew_batch.job.article.externalApi.naver;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.monew.monew_batch.article.naver.NaverApiService;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

@SpringBootTest
@ActiveProfiles("test")
public class NaverApiServiceTest {

	@Autowired
	private NaverApiService naverApiService;

	@Test
	@DisplayName("네이버 api 데이터 한번 가져와 보기 ")
	public void getNewsTest() {
		List<ArticleSaveDto> news = naverApiService.getNews("윤석열", 10, 1, "sim");

		for (ArticleSaveDto article : news) {
			System.out.println(article.getTitle());
			System.out.println(article.getPublishDate());
			System.out.println(article.getSummary());
			System.out.println(article.getSource());
			System.out.println(article.getSourceUrl());
			System.out.println("===============================");
		}
	}
}
