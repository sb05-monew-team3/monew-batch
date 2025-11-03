package com.monew.monew_batch.job.api_article_collection.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.mapper.ArticleMapper;
import com.monew.monew_batch.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverArticleApiWriter implements ItemWriter<ArticleSaveDto> {

	private final ArticleRepository articleRepository;
	private final ArticleMapper articleMapper;

	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public void write(Chunk<? extends ArticleSaveDto> items) {
		if (items == null || items.isEmpty()) {
			return;
		}

		List<Article> articles = new ArrayList<>();
		for (ArticleSaveDto item : items) {
			Article entity = articleMapper.toEntity(item);
			articles.add(entity);
		}

		updateProcessedCount(articles.size());

		articleRepository.saveAll(articles);
	}

	private void updateProcessedCount(int count) {
		if (stepExecution == null) {
			return;
		}

		long processedCount = stepExecution.getExecutionContext().getLong("processedCount", 0L);
		stepExecution.getExecutionContext().putLong("processedCount", processedCount + count);
	}

}
