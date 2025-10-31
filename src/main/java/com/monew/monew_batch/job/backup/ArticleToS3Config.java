package com.monew.monew_batch.job.backup;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.job.JobName;
import com.monew.monew_batch.job.common.listener.ArticleBackupSkipListener;
import com.monew.monew_batch.job.common.listener.JobProcessedCountListener;
import com.monew.monew_batch.job.common.writer.ArticleToS3Writer;
import com.monew.monew_batch.repository.ArticleRepository;
import com.monew.monew_batch.storage.BinaryStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ArticleToS3Config {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final ArticleRepository articleRepository;
	private final BinaryStorage binaryStorage;

	private final JobProcessedCountListener jobProcessedCountListener;
	private final ArticleToS3Writer articleWriter;

	@Bean
	@StepScope
	public RepositoryItemReader<Article> articleReader() {
		log.info("[기사 백업] Reader 생성: 모든 Article 조회");

		int chunkSize = 100;
		return new RepositoryItemReaderBuilder<Article>()
			.name("articleReader")
			.repository(articleRepository)
			.methodName("findAll")
			.pageSize(chunkSize)
			.sorts(Map.of("id", Sort.Direction.ASC))
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<Article, Article> articleProcessor() {
		return article -> {
			if (binaryStorage.exists(article.getId(), article.getPublishDate())) {
				log.info("[기사 백업] 이미 존재함(건너뜀): uuid={}, title={}", article.getId(), article.getTitle());
				return null;
			}
			log.debug("[기사 백업] 처리 중: uuid={}, title={}", article.getId(), article.getTitle());
			return article;
		};
	}

	@Bean
	public Step articleBackupStep() {
		return new StepBuilder("articleBackupStep", jobRepository)
			.<Article, Article>chunk(100, platformTransactionManager)
			.reader(articleReader())
			.processor(articleProcessor())
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
