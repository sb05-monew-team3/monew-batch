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
public class NaverArticleApiProcessor implements ItemProcessor<NaverArticleProcessorDto, NaverArticleProcessorDto> {

	private final ArticleRepository articleRepository;
	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public NaverArticleProcessorDto process(NaverArticleProcessorDto item) throws Exception {

		String keyword = item.getKeyword();
		ArticleSaveDto articleSaveDto = item.getArticleSaveDto();

		if (articleRepository.existsBySourceUrl(articleSaveDto.getSourceUrl())) {
			return null;
		}

		ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
		int currentCount = jobContext.containsKey(keyword) ? jobContext.getInt(keyword) : 0;
		jobContext.putInt(keyword, currentCount + 1);

		return item;
	}
}

