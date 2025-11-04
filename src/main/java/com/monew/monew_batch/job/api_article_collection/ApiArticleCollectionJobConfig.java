package com.monew.monew_batch.job.api_article_collection;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.monew.monew_batch.entity.Interest;
import com.monew.monew_batch.job.JobName;
import com.monew.monew_batch.job.api_article_collection.dto.NaverArticleProcessorDto;
import com.monew.monew_batch.job.api_article_collection.processor.InterestProcessor;
import com.monew.monew_batch.job.api_article_collection.processor.NaverArticleApiProcessor;
import com.monew.monew_batch.job.api_article_collection.reader.InterestReader;
import com.monew.monew_batch.job.api_article_collection.reader.NaverArticleApiReader;
import com.monew.monew_batch.job.api_article_collection.writer.NaverArticleApiWriter;
import com.monew.monew_batch.job.api_article_collection.writer.NotificationWriter;
import com.monew.monew_batch.job.common.listener.JobProcessedCountListener;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiArticleCollectionJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	private final NaverArticleApiReader naverArticleApiReader;
	private final NaverArticleApiWriter naverArticleApiWriter;
	private final NaverArticleApiProcessor naverArticleApiProcessor;

	private final InterestReader interestReader;
	private final InterestProcessor interestProcessor;
	private final NotificationWriter notificationWriter;

	private final JobProcessedCountListener jobProcessedCountListener;

	@Bean
	public Step naverNewsStep() {
		int chunk = 100;

		return new StepBuilder("naverNewsStep", jobRepository)
			.<NaverArticleProcessorDto, NaverArticleProcessorDto>chunk(chunk, platformTransactionManager)
			.reader(naverArticleApiReader)
			.processor(naverArticleApiProcessor)
			.writer(naverArticleApiWriter)
			.build();
	}

	@Bean
	public Step interestStep() {
		int chunk = 100;

		return new StepBuilder("addNotificationStep", jobRepository)
			.<Interest, Interest>chunk(chunk, platformTransactionManager)
			.reader(interestReader)
			.processor(interestProcessor)
			.writer(notificationWriter)
			.build();
	}

	@Bean
	public Job apiArticleCollectionJob() {
		return new JobBuilder(JobName.API_ARTICLE_COLLECTION_JOB.getName(), jobRepository)
			.listener(jobProcessedCountListener)
			.start(naverNewsStep())
			.next(interestStep())
			.build();
	}
}
