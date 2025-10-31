package com.monew.monew_batch.job.common.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArticleDedupProcessor implements ItemProcessor<ArticleSaveDto, ArticleSaveDto> {

	private final ArticleRepository articleRepository;

	@Override
	public ArticleSaveDto process(ArticleSaveDto item) {

		// 이거 N+1 문제 일어남 나중에 해결할 것
		// JPA는 n+1 문제가 많음
		// 마이바티스, jdbc랑 성능비교해보는게 좋을 듯
		// 이거 ppt에 넣으면 좋을듯
		// 월등하다고함

		if (articleRepository.existsBySourceUrl(item.getSourceUrl())) {
			return null;
		}

		return item;
	}
}
