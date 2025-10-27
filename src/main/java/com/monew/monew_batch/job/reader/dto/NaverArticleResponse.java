package com.monew.monew_batch.job.reader.dto;

import java.util.List;

import lombok.Data;

@Data
public class NaverArticleResponse {
	String lastBuildDate;
	int total;
	int start;
	int display;
	List<ArticleItem> items;

	@Data
	public static class ArticleItem {
		String title;
		String originallink;
		String link;
		String description;
		String pubDate;
	}
}