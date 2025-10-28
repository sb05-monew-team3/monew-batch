package com.monew.monew_batch.job;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monew.monew_batch.entity.Article;
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
	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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
	@StepScope
	public ItemWriter<Article> articleWriter() {
		return items -> {
			for (Article article : items) {
				try {
					byte[] articleByte = objectMapper.writeValueAsBytes(article);

					binaryStorage.put(article.getId(), article.getPublishDate(), articleByte);
					log.debug("[기사 백업] 저장 완료: id={}", article.getId());
				} catch (Exception e) {
					log.error("[기사 백업] 저장 완료: id={}, error={}", article.getId(), e.getMessage());
					throw new RuntimeException("[기사 백업] 실패", e);
				}
			}
			log.info("[기사 백업] {}건 저장 완료", items.size());
		};
	}

	@Bean
	public Step articleBackupStep() {
		return new StepBuilder("articleBackupStep", jobRepository)
			.<Article, Article>chunk(100, platformTransactionManager)
			.reader(articleReader())
			.processor(articleProcessor())
			.writer(articleWriter())
			.build();
	}

	@Bean
	public Job articleBackUpJob() {
		return new JobBuilder("articleBackupJob", jobRepository)
			.start(articleBackupStep())
			.build();
	}
}
