package com.monew.monew_batch.job.api_article_collection.dto;

import com.monew.monew_batch.job.common.dto.ArticleSaveDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NaverArticleProcessorDto {
	String keyword;
	ArticleSaveDto articleSaveDto;
}
