package com.monew.monew_batch.job;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ArticleCollectionJobConfig {

	private JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	// @Bean
	// public Job articleCollectionJob() {
	//
	// }

}
