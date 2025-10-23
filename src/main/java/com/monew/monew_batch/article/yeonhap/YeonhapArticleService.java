package com.monew.monew_batch.article.yeonhap;

import java.util.List;

import com.monew.monew_batch.article.RssArticleService;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

public class YeonhapArticleService implements RssArticleService {

	@Override
	public List<ArticleSaveDto> getArticleList(String feed) {
		return List.of();
	}

}
