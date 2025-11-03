package com.monew.monew_batch.job.api_article_collection.processor;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.job.api_article_collection.dto.NaverArticleProcessorDto;
import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverArticleApiProcessor implements ItemProcessor<NaverArticleProcessorDto, ArticleSaveDto> {

	private final ArticleRepository articleRepository;
	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public ArticleSaveDto process(NaverArticleProcessorDto item) throws Exception {

		String keyword = item.getKeyword();
		ArticleSaveDto articleSaveDto = item.getArticleSaveDto();

		// 이거 N+1 문제 일어남 나중에 해결할 것
		// JPA는 n+1 문제가 많음
		// 마이바티스, jdbc랑 성능비교해보는게 좋을 듯
		// 이거 ppt에 넣으면 좋을듯
		// 월등하다고함
		if (articleRepository.existsBySourceUrl(articleSaveDto.getSourceUrl())) {
			// log.info("[네이버 기사 수집] 이미 존재하는 데이터(키워드 = {}, url = {})", keyword, articleSaveDto.getSourceUrl());
			return null;
		}

		ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();

		int currentCount = jobContext.containsKey(keyword) ? jobContext.getInt(keyword) : 0;

		jobContext.putInt(keyword, currentCount + 1);

		return articleSaveDto;
	}
}

