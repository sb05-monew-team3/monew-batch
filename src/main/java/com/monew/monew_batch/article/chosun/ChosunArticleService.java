package com.monew.monew_batch.article.chosun;

import java.util.List;

import com.monew.monew_batch.article.RssArticleService;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

public class ChosunArticleService implements RssArticleService {

	@Override
	public List<ArticleSaveDto> getArticleList(String feed) {
		return List.of();
	}

}
