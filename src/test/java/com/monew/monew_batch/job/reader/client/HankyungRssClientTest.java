package com.monew.monew_batch.job.reader.client;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.monew.monew_batch.job.dto.ArticleSaveDto;

@SpringBootTest
public class HankyungRssClientTest {

	@Autowired
	HankyungRssClient hankyungRssClient;

	@Test
	public void fetchArticlesTest() {
		List<ArticleSaveDto> economy = hankyungRssClient.fetchArticles("economy");
		if (economy == null) {
			System.out.println("null값임 ");
		}
		for (ArticleSaveDto articleSaveDto : economy) {
			System.out.println(articleSaveDto.getTitle());
			System.out.println(articleSaveDto.getPublishDate());
			System.out.println(articleSaveDto.getSource());
			System.out.println(articleSaveDto.getSummary());
			System.out.println(articleSaveDto.getPublishDate());
			System.out.println(articleSaveDto.getSourceUrl());
		}
	}

}
