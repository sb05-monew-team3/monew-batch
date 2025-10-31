package com.monew.monew_batch.job.api_article_collection.reader;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.job.api_article_collection.client.NaverApiClient;
import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.repository.InterestKeywordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverArticleApiReader implements ItemStreamReader<ArticleSaveDto> {

	private final NaverApiClient naverApiClient;
	private final InterestKeywordRepository interestKeywordRepository;

	private static final String SORT = "sim";
	private static final int DISPLAY = 100;
	private static final int MAX_START = 1000;
	private static final String CURRENT_PAGE_KEY = "currentPage";

	private List<String> keywords;
	private Iterator<ArticleSaveDto> articleBufferIterator;
	private int currentPage = 1;     //
	private int keywordIndex = 0;    // 현재 키워드 위치(에러 시 처음부터 재시작 )
	private int start;

	@Override
	public void open(ExecutionContext executionContext) {
		if (keywords == null) {
			keywords = interestKeywordRepository.findDistinctNames();
			if (keywords == null || keywords.isEmpty()) {
				log.info("[네이버 기사 주기 작업] 키워드 없음");
				return;
			}
			log.info("[네이버 기사 주기 작업] 키워드 로드: {}", keywords);
		}

		JobParameters params = Objects.requireNonNull(StepSynchronizationManager.getContext())
			.getStepExecution()
			.getJobParameters();
		Long page = params.getLong("page");
		if (page != null && page > 0) {
			currentPage = page.intValue();
			log.info("[네이버 기사 주기 작업] JobParameters.page 적용: {}", currentPage);
		}

		start = (currentPage - 1) * DISPLAY + 1;
		if (start > MAX_START) {
			log.info("[네이버 기사 주기 작업] 상한 도달로 page 리셋 : {} -> 1", currentPage);
			currentPage = 1;
			executionContext.putInt(CURRENT_PAGE_KEY, 1);
		}

		log.info("[네이버 기사 주기 작업] open(): currentPage={}", currentPage);
	}

	@Override
	public ArticleSaveDto read() {

		while (articleBufferIterator == null || !articleBufferIterator.hasNext()) {
			// 현재 page에 대해 모든 키워드 처리 완료 : page 올리고 저장 후 종료 -> 다음 페이지로 넘어가야함
			if (keywordIndex >= keywords.size()) {
				int nextPage = currentPage + 1;

				ExecutionContext ec = Objects.requireNonNull(StepSynchronizationManager
						.getContext())
					.getStepExecution()
					.getExecutionContext();
				ec.putInt(CURRENT_PAGE_KEY, nextPage);
				currentPage = nextPage;
				articleBufferIterator = null; // 버퍼 초기화
				keywordIndex = 0; // 키워드 초기화

				log.info("[네이버 기사 주기 작업] page={} 완료 : page={}로 증가, 종료", nextPage - 1, nextPage);
				return null;
			}

			String keyword = keywords.get(keywordIndex++);
			log.info("[네이버 기사 주기 작업] '{}' 수집 시작: page={}, start={}, display={}, sort={}", keyword, currentPage, start,
				DISPLAY, SORT);

			try {
				List<ArticleSaveDto> articles = naverApiClient.fetchArticles(keyword, DISPLAY, start, SORT); // 기사 가져오기

				if (articles == null || articles.isEmpty()) {
					log.info("[네이버 기사 주기 작업] '{}' 결과 없음 (page={}, start={})", keyword, currentPage, start);
					continue;
				}

				articleBufferIterator = articles.iterator();
			} catch (Exception e) {
				log.error("[네이버 기사 주기 작업] '{}' (page={}, start={}) 수집 오류: {}", keyword, currentPage, start,
					e.getMessage(), e);
				throw new RuntimeException("네이버 API 호출 실패(재시작 시 page 유지, 키워드는 처음부터)", e);
			}
		}

		return articleBufferIterator.next();
	}
}