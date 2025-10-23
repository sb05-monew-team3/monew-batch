package com.monew.monew_batch.article;

import java.util.List;

import com.monew.monew_batch.writer.dto.ArticleSaveDto;

public interface RssArticleService {
	List<ArticleSaveDto> getArticleList(String feed);
}
