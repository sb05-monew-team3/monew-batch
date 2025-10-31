package com.monew.monew_batch.job.common.dto;

import java.time.Instant;

import com.monew.monew_batch.entity.ArticleSource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleSaveDto {
	ArticleSource source; // 네이버, 한경, ...
	String sourceUrl;
	String title;
	Instant publishDate;
	String summary;
}
