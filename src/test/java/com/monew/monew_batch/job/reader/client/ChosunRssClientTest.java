package com.monew.monew_batch.job.reader.client;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.monew.monew_batch.job.dto.ArticleSaveDto;

@SpringBootTest
public class ChosunRssClientTest {

	@Autowired
	ChosunRssClient chosunRssClient;

	@Test
	public void fetchArticlesTest() {
		List<ArticleSaveDto> articleSaveDtos = chosunRssClient.fetchArticles("politics");
		if (articleSaveDtos == null) {
			System.out.println("데이터 없음");
		} else {
			for (ArticleSaveDto articleSaveDto : articleSaveDtos) {
				System.out.println("============================================");
				System.out.println("title: " + articleSaveDto.getTitle());
				System.out.println("publishDate: " + articleSaveDto.getPublishDate());
				System.out.println("source: " + articleSaveDto.getSource());
				System.out.println("summary: " + articleSaveDto.getSummary());
				System.out.println("sourceUrl: " + articleSaveDto.getSourceUrl());
			}
		}

	}
}
