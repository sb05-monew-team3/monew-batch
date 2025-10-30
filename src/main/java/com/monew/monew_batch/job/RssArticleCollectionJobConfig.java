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
import com.monew.monew_batch.job.listener.JobProcessedCountListener;
import com.monew.monew_batch.job.processor.ArticleDedupProcessor;
import com.monew.monew_batch.job.reader.ChosunArticleRssReader;
import com.monew.monew_batch.job.reader.HankyungArticleRssReader;
import com.monew.monew_batch.job.reader.YonhapArticleRssReader;
import com.monew.monew_batch.job.writer.ArticleItemWriter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RssArticleCollectionJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	private final ChosunArticleRssReader chosunArticleRssReader;
	private final HankyungArticleRssReader hankyungArticleRssReader;
	private final YonhapArticleRssReader yonhapArticleRssReader;

	private final ArticleDedupProcessor articleDedupProcessor;
	private final ArticleItemWriter articleItemWriter;

	private final JobProcessedCountListener jobProcessedCountListener;
	private int chunk = 100;

	/**
	 * 스프링 배치-> 잡 인스턴스가 중단되면 어떻게되나
	 * 중단 된후 어떻게 재개할 수 있을까?
	 */

	@Bean
	public Step yonhapNewsStep() {
		return new StepBuilder("yonhapArticleStep", jobRepository)
			.<ArticleSaveDto, ArticleSaveDto>chunk(chunk, platformTransactionManager)
			.reader(yonhapArticleRssReader)
			.processor(articleDedupProcessor)
			.writer(articleItemWriter)
			.build();
	}

	@Bean
	public Step hankyungNewsStep() {
		return new StepBuilder("hankyungArticleStep", jobRepository)
			.<ArticleSaveDto, ArticleSaveDto>chunk(chunk, platformTransactionManager)
			.reader(hankyungArticleRssReader)
			.processor(articleDedupProcessor)
			.writer(articleItemWriter)
			.build();
	}

	@Bean
	public Step chosunNewsStep() {
		return new StepBuilder("chosunArticleStep", jobRepository)
			.<ArticleSaveDto, ArticleSaveDto>chunk(chunk, platformTransactionManager)
			.reader(chosunArticleRssReader)
			.processor(articleDedupProcessor)
			.writer(articleItemWriter)
			.build();
	}

	@Bean
	public Job rssArticleCollectionJob() {
		return new JobBuilder(JobName.RSS_ARTICLE_COLLECTION_JOB.getName(), jobRepository)
			.listener(jobProcessedCountListener)
			.start(hankyungNewsStep())
			.next(chosunNewsStep())
			.next(yonhapNewsStep())
			.build();
	}

}
