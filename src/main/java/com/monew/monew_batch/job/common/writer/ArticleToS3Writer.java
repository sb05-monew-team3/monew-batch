package com.monew.monew_batch.job.common.writer;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.storage.BinaryStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleToS3Writer implements ItemWriter<Article> {

	private final BinaryStorage binaryStorage;
	private final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule());

	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public void write(Chunk<? extends Article> chunk) throws Exception {
		if (chunk == null || chunk.isEmpty()) {
			return;
		}

		int successCount = 0;

		for (Article article : chunk) {
			try {
				byte[] articleByte = objectMapper.writeValueAsBytes(article);
				binaryStorage.put(article.getId(), article.getPublishDate(), articleByte);
				successCount++;

				log.debug("[기사 백업] 저장 완료: id={}", article.getId());
			} catch (Exception e) {
				log.error("[기사 백업] 저장 실패: id={}, error={}",
					article.getId(), e.getMessage());
				throw new RuntimeException("[기사 백업] 실패: " + article.getId(), e);
			}
		}

		updateProcessedCount(successCount);
		log.info("[기사 백업] {} 건 저장 완료", successCount);
	}

	private void updateProcessedCount(int count) {
		if (stepExecution != null) {
			long currentCount = stepExecution.getExecutionContext()
				.getLong("processedCount", 0L);
			stepExecution.getExecutionContext()
				.putLong("processedCount", currentCount + count);
		}
	}
}