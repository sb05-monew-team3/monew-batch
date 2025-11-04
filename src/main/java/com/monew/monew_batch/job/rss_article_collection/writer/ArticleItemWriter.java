package com.monew.monew_batch.job.rss_article_collection.writer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.entity.ArticleInterest;
import com.monew.monew_batch.entity.Interest;
import com.monew.monew_batch.entity.InterestKeyword;
import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.mapper.ArticleMapper;
import com.monew.monew_batch.repository.ArticleInterestRepository;
import com.monew.monew_batch.repository.ArticleRepository;
import com.monew.monew_batch.repository.InterestKeywordRepository;
import com.monew.monew_batch.storage.BinaryStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleItemWriter implements ItemWriter<ArticleSaveDto> {

	private final ArticleRepository articleRepository;
	private final ArticleMapper articleMapper;
	private final ArticleInterestRepository articleInterestRepository;
	private final InterestKeywordRepository interestKeywordRepository;

	private final BinaryStorage binaryStorage;
	private final ObjectMapper objectMapper;

	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	@Transactional
	public void write(Chunk<? extends ArticleSaveDto> items) throws JsonProcessingException {
		int savedCount = 0;

		if (items == null || items.isEmpty()) {
			return;
		}

		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext jobContext = jobExecution.getExecutionContext();

		List<String> keywords = interestKeywordRepository.findDistinctNames();

		for (ArticleSaveDto item : items) {
			Article entity = articleMapper.toEntity(item);

			Set<Interest> matchedInterests = new HashSet<>();
			Set<String> matchedKeywords = new HashSet<>();

			for (String keyword : keywords) {
				if (entity.getTitle().contains(keyword)) {
					matchedKeywords.add(keyword);

					int currentCount = jobContext.containsKey(keyword) ? jobContext.getInt(keyword) : 0;
					jobContext.putInt(keyword, currentCount + 1);

					List<InterestKeyword> interestKeywords = interestKeywordRepository.findByName(keyword);

					for (InterestKeyword interestKeyword : interestKeywords) {
						matchedInterests.add(interestKeyword.getInterest());
					}
				}
			}

			if (matchedInterests.isEmpty()) {
				log.info("매칭된 키워드 없음. Article 저장 스킵: title={}", entity.getTitle());
				continue;
			}

			Article savedArticle = articleRepository.save(entity);
			savedCount++;
			log.info("Article 저장 완료: id={}, title={}", savedArticle.getId(), savedArticle.getTitle());

			// for (String keyword : matchedKeywords) {
			// 	backUp(savedArticle, keyword);
			// }

			for (Interest interest : matchedInterests) {
				ArticleInterest articleInterest = ArticleInterest.builder()
					.article(savedArticle)
					.interest(interest)
					.build();

				articleInterestRepository.save(articleInterest);

				log.info("ArticleInterest 저장 완료: articleId={}, interestId={}, interestName={}",
					savedArticle.getId(), interest.getId(), interest.getName());
			}

			log.info("Article 처리 완료: id={}, 매칭된 Interest 수={}",
				savedArticle.getId(), matchedInterests.size());
		}

		updateProcessedCount(savedCount);
	}

	private void updateProcessedCount(int count) {
		if (stepExecution == null) {
			return;
		}

		long processedCount = stepExecution.getExecutionContext().getLong("processedCount", 0L);
		stepExecution.getExecutionContext().putLong("processedCount", processedCount + count);
	}

	private void backUp(Article article, String keyword) throws JsonProcessingException {

		if (binaryStorage.exists(article.getId(), keyword, article.getPublishDate())) {
			return;
		}

		byte[] bytes = objectMapper.writeValueAsBytes(article);
		binaryStorage.put(article.getId(), article.getPublishDate(), keyword, bytes);
	}
}
