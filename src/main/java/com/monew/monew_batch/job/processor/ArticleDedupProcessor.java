package com.monew.monew_batch.job.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.job.dto.ArticleSaveDto;
import com.monew.monew_batch.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArticleDedupProcessor implements ItemProcessor<ArticleSaveDto, ArticleSaveDto> {

	private final ArticleRepository articleRepository;

	@Override
	public ArticleSaveDto process(ArticleSaveDto item) throws Exception {

		if (articleRepository.existsBySourceUrl(item.getSourceUrl())) {
			return null;
		}

		return item;
	}
}
