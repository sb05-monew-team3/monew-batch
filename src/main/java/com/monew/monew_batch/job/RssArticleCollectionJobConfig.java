package com.monew.monew_batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.monew.monew_batch.job.dto.ArticleSaveDto;
import com.monew.monew_batch.job.processor.ArticleDedupProcessor;
import com.monew.monew_batch.job.reader.HankyungArticleRssReader;
import com.monew.monew_batch.job.writer.ArticleItemWriter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RssArticleCollectionJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	private final HankyungArticleRssReader hankyungArticleRssReader;
	private final ArticleDedupProcessor articleDedupProcessor;
	private final ArticleItemWriter articleItemWriter;

	@Bean
	public Step hankyungNewsStep() {
		int chunk = 100;

		return new StepBuilder("hankyungNewsStep", jobRepository)
			.<ArticleSaveDto, ArticleSaveDto>chunk(chunk, platformTransactionManager)
			.reader(hankyungArticleRssReader)
			.processor(articleDedupProcessor)
			.writer(articleItemWriter)
			.build();
	}

	@Bean
	public Job rssArticleCollectionJob() {
		return new JobBuilder("rssArticleCollectionJob", jobRepository)
			.start(hankyungNewsStep())
			.build();
	}

}
