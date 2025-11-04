package com.monew.monew_batch.job.backup;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.job.JobName;
import com.monew.monew_batch.job.backup.dto.ArticleBackUpDto;
import com.monew.monew_batch.job.backup.listener.ArticleBackupSkipListener;
import com.monew.monew_batch.job.backup.processor.ArticleBackupProcessor;
import com.monew.monew_batch.job.backup.reader.ArticleBackupReader;
import com.monew.monew_batch.job.backup.writer.ArticleBackupWriter;
import com.monew.monew_batch.job.common.listener.JobProcessedCountListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ArticleToS3Config {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	private final JobProcessedCountListener jobProcessedCountListener;
	private final ArticleBackupReader articleBackupReader;
	private final ArticleBackupProcessor articleBackupProcessor;
	private final ArticleBackupWriter articleWriter;

	@Bean
	public Step articleBackupStep() {
		return new StepBuilder("articleBackupStep", jobRepository)
			.<Article, ArticleBackUpDto>chunk(100, platformTransactionManager)
			.reader(articleBackupReader)
			.processor(articleBackupProcessor)
			.writer(articleWriter)
			.faultTolerant()
			.retryLimit(3)
			.retry(RuntimeException.class)
			.skipLimit(50)
			.skip(Exception.class)
			.listener(new ArticleBackupSkipListener())
			.build();
	}

	@Bean
	public Job articleBackUpJob() {
		return new JobBuilder(JobName.ARTICLE_BACKUP_TO_S3_JOB.getName(), jobRepository)
			.listener(jobProcessedCountListener)
			.start(articleBackupStep())
			.build();
	}
}
