package com.monew.monew_batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.monew.monew_batch.job.dto.ArticleSaveDto;
import com.monew.monew_batch.job.listener.JobProcessedCountListener;
import com.monew.monew_batch.job.processor.ArticleDedupProcessor;
import com.monew.monew_batch.job.reader.NaverArticleApiReader;
import com.monew.monew_batch.job.writer.ArticleItemWriter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiArticleCollectionJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	private final NaverArticleApiReader naverArticleApiReader;
	private final ArticleDedupProcessor articleDedupProcessor;
	private final ArticleItemWriter articleItemWriter;

	private final JobProcessedCountListener jobProcessedCountListener;

	// 이게 뭐지
	@Bean
	public ExecutionContextPromotionListener pagePromotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys(new String[] {"currentPage"});
		listener.setStrict(false);
		return listener;
	}

	@Bean
	public Step naverNewsStep() {
		int chunk = 100;

		return new StepBuilder("naverNewsStep", jobRepository)
			.<ArticleSaveDto, ArticleSaveDto>chunk(chunk, platformTransactionManager)
			.reader(naverArticleApiReader)
			.processor(articleDedupProcessor)
			.writer(articleItemWriter)
			.listener(pagePromotionListener())
			.build();
	}

	@Bean
	public Job apiArticleCollectionJob() {
		return new JobBuilder(JobName.API_ARTICLE_COLLECTION_JOB.getName(), jobRepository)
			.listener(jobProcessedCountListener)
			.start(naverNewsStep())
			.build();
	}
}
