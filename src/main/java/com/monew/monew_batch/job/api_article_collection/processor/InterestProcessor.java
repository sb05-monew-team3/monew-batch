package com.monew.monew_batch.job.api_article_collection.processor;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.entity.Interest;
import com.monew.monew_batch.entity.InterestKeyword;
import com.monew.monew_batch.repository.InterestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class InterestProcessor implements ItemProcessor<Interest, Interest> {

	private final InterestRepository interestRepository;
	private StepExecution stepExecution;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		log.info("[네이버 기사 알림 추가] Processor 초기화 완료");
	}

	@Override
	public Interest process(Interest input) {
		log.info("[네이버 기사 알림 추가] 관심사 처리 시작 - ID: {}", input.getId());

		Interest interest = interestRepository.findByIdWithKeywords(input.getId()).orElseThrow();

		int sumOfAddedCount = 0;
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

		for (InterestKeyword interestKeyword : interest.getInterestKeywords()) {
			String k = interestKeyword.getName();
			int count = executionContext.containsKey(k) ? executionContext.getInt(k) : 0;
			sumOfAddedCount += count;
			log.info("[네이버 기사 알림 추가] 키워드 '{}' - 추가된 기사: {}건", k, count);
		}

		log.info("[네이버 기사 알림 추가] 관심사 '{}' - 총 {}건의 새 기사", interest.getName(), sumOfAddedCount);

		// 기사 수를 Interest 객체에 저장 (임시)
		interest.setArticleCount(sumOfAddedCount); // 필드 추가 필요

		return interest;
	}
}