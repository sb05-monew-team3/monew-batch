package com.monew.monew_batch.reader.dto;

import java.util.List;

import lombok.Data;

@Data
public class NaverArticleResponse {
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