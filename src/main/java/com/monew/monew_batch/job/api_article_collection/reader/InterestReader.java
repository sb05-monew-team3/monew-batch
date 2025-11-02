package com.monew.monew_batch.job.api_article_collection.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.entity.Interest;
import com.monew.monew_batch.repository.InterestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterestReader implements ItemStreamReader<Interest> {

	private final InterestRepository interestRepository;

	private List<Interest> interests;
	private Iterator<Interest> interestIterator;

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (interests == null) {
			log.info("[네이버 기사 알림 추가] 관심사 조회 시작");
			interests = interestRepository.findAll();

			if (interests.isEmpty()) {
				log.warn("[네이버 기사 알림 추가] 처리할 관심사가 없습니다.");
				return;
			}

			log.info("[네이버 기사 알림 추가] 총 {}개의 관심사 조회 완료", interests.size());
			interestIterator = interests.iterator();
		}
	}

	@Override
	public Interest read() {
		if (interestIterator == null || !interestIterator.hasNext()) {
			log.info("[네이버 기사 알림 추가] 관심사 읽기 완료");
			return null;
		}

		Interest interest = interestIterator.next();
		log.info("[네이버 기사 알림 추가] 관심사 읽기 - ID: {}, 이름: {}", interest.getId(), interest.getName());
		return interest;
	}

	@Override
	public void close() {
		log.info("[네이버 기사 알림 추가] Reader 종료");
		interests = null;
		interestIterator = null;
	}
}