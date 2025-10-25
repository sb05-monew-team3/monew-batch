package com.monew.monew_batch.repository;

import java.time.Instant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.entity.ArticleSource;

@SpringBootTest
@Transactional
public class ArticleRepositoryTest {

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	@DisplayName("Article 저장 테스트")
	public void saveTest() {
		Article article = Article.builder()
			.source(ArticleSource.NAVER)
			.title("testTitle")
			.publishDate(Instant.now())
			.summary("요약입니다.")
			.sourceUrl("http:localhost:8080")
			.build();
		articleRepository.save(article);
	}
}
