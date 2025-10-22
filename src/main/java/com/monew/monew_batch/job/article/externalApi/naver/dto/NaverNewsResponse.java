package com.monew.monew_batch.job.article.externalApi.naver.dto;

import java.util.List;

import lombok.Data;

@Data
public class NaverNewsResponse {
	String lastBuildDate;
	int total;
	int start;
	int display;
	List<NewsItem> items;

	@Data
	public static class NewsItem {
		String title;
		String originallink;
		String link;
		String description;
		String pubDate;
	}
}