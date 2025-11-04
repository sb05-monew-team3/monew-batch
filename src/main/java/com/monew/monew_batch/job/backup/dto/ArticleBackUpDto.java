package com.monew.monew_batch.job.backup.dto;

import com.monew.monew_batch.entity.Article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleBackUpDto {
	Article article;
	String interestName;
}
