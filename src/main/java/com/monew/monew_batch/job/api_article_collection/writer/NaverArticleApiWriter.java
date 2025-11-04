package com.monew.monew_batch.job.api_article_collection.writer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.entity.ArticleInterest;
import com.monew.monew_batch.entity.Interest;
import com.monew.monew_batch.entity.InterestKeyword;
import com.monew.monew_batch.job.api_article_collection.dto.NaverArticleProcessorDto;
import com.monew.monew_batch.mapper.ArticleMapper;
import com.monew.monew_batch.repository.ArticleInterestRepository;
import com.monew.monew_batch.repository.ArticleRepository;
import com.monew.monew_batch.repository.InterestKeywordRepository;
import com.monew.monew_batch.repository.InterestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverArticleApiWriter implements ItemWriter<NaverArticleProcessorDto> {

	private final ArticleInterestRepository articleInterestRepository;
	private final InterestRepository interestRepository;
	private final ArticleRepository articleRepository;
	private final ArticleMapper articleMapper;
	private final InterestKeywordRepository interestKeywordRepository;

	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	@Transactional
	public void write(Chunk<? extends NaverArticleProcessorDto> naverArticleProcessorDtos) {
		if (naverArticleProcessorDtos == null || naverArticleProcessorDtos.isEmpty()) {
			return;
		}

		for (NaverArticleProcessorDto naverArticleProcessorDto : naverArticleProcessorDtos) {
			Article entity = articleMapper.toEntity(naverArticleProcessorDto.getArticleSaveDto());
			Article savedArticle = articleRepository.save(entity);

			log.info("Article 저장 완료: id={}, title={}", savedArticle.getId(), savedArticle.getTitle());

			List<InterestKeyword> interestKeywords = interestKeywordRepository.findByName(
				naverArticleProcessorDto.getKeyword());

			if (interestKeywords.isEmpty()) {
				log.warn("키워드 '{}' 에 해당하는 Interest를 찾을 수 없습니다.", naverArticleProcessorDto.getKeyword());
				continue;
			}

			Set<UUID> processedInterestIds = new HashSet<>();

			for (InterestKeyword interestKeyword : interestKeywords) {
				Interest interest = interestKeyword.getInterest();

				if (processedInterestIds.contains(interest.getId())) {
					continue;
				}

				ArticleInterest articleInterest = ArticleInterest.builder()
					.article(savedArticle)
					.interest(interest)
					.build();

				articleInterestRepository.save(articleInterest);
				processedInterestIds.add(interest.getId());

				log.info("ArticleInterest 저장 완료: articleId={}, interestId={}, interestName={}",
					savedArticle.getId(), interest.getId(), interest.getName());
			}
		}

		updateProcessedCount(naverArticleProcessorDtos.size());
	}

	private void updateProcessedCount(int count) {
		if (stepExecution == null) {
			return;
		}

		long processedCount = stepExecution.getExecutionContext().getLong("processedCount", 0L);
		stepExecution.getExecutionContext().putLong("processedCount", processedCount + count);
	}

}
