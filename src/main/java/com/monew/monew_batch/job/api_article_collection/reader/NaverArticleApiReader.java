package com.monew.monew_batch.job.api_article_collection.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.job.api_article_collection.client.NaverApiClient;
import com.monew.monew_batch.job.api_article_collection.dto.NaverArticleProcessorDto;
import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.repository.InterestKeywordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverArticleApiReader implements ItemStreamReader<NaverArticleProcessorDto> {

	private final NaverApiClient naverApiClient;
	private final InterestKeywordRepository interestKeywordRepository;

	private static final String SORT = "sim";
	private static final int DISPLAY = 100;     // 네이버 최대 100
	private static final int MAX_START = 1000;  // 네이버 최대 start
	private static final String EC_KEYWORD_INDEX = "naver.keywordIndex";
	private static final String EC_CURRENT_START = "naver.currentStart";

	private List<String> keywords;
	private Iterator<NaverArticleProcessorDto> articleBufferIterator;

	private int keywordIndex = 0;
	private int currentStart = 1;

	@Override
	public void open(ExecutionContext executionContext) {
		if (keywords == null) {
			keywords = interestKeywordRepository.findDistinctNames();
			if (keywords.isEmpty()) {
				log.info("[네이버 기사 수집] 키워드 없음");
				return;
			}
			log.info("[네이버 기사 수집] 키워드 로드: {}", keywords);
		}

		if (executionContext != null) {
			if (executionContext.containsKey(EC_KEYWORD_INDEX)) {
				keywordIndex = executionContext.getInt(EC_KEYWORD_INDEX);
			}
			if (executionContext.containsKey(EC_CURRENT_START)) {
				currentStart = executionContext.getInt(EC_CURRENT_START);
			}
		}

		keywordIndex = Math.min(Math.max(keywordIndex, 0), keywords.size());
		if (currentStart < 1 || currentStart > MAX_START)
			currentStart = 1;
	}

	@Override
	public NaverArticleProcessorDto read() {
		if (articleBufferIterator != null && articleBufferIterator.hasNext()) {
			return articleBufferIterator.next();
		}

		if (keywords == null || keywordIndex >= keywords.size()) {
			return null;
		}

		while (keywordIndex < keywords.size()) {
			String keyword = keywords.get(keywordIndex);

			if (currentStart > MAX_START) {
				moveToNextKeyword();
				continue;
			}

			List<ArticleSaveDto> articles;
			articles = naverApiClient.fetchArticles(keyword, DISPLAY, currentStart, SORT);

			int requested = DISPLAY;
			int received = (articles == null) ? 0 : articles.size();
			log.info("[네이버 기사 수집] '{}' start={} 요청={}건 응답={}건", keyword, currentStart, requested, received);

			currentStart += DISPLAY;

			if (received == 0) {
				moveToNextKeyword();
				continue;
			}

			List<NaverArticleProcessorDto> buffer = new ArrayList<>(received);
			for (ArticleSaveDto a : articles) {
				if (a == null)
					continue;
				buffer.add(new NaverArticleProcessorDto(keyword, a));
			}
			if (buffer.isEmpty()) {
				moveToNextKeyword();
				continue;
			}

			if (received < DISPLAY || currentStart > MAX_START) {
			}

			articleBufferIterator = buffer.iterator();
			if (articleBufferIterator.hasNext()) {
				return articleBufferIterator.next();
			}
		}

		return null;
	}

	@Override
	public void update(ExecutionContext executionContext) {
		if (executionContext == null)
			return;
		executionContext.putInt(EC_KEYWORD_INDEX, keywordIndex);
		executionContext.putInt(EC_CURRENT_START, currentStart);
	}

	@Override
	public void close() {
		keywords = null;
		articleBufferIterator = null;
		keywordIndex = 0;
		currentStart = 1;
	}

	private void moveToNextKeyword() {
		keywordIndex++;
		currentStart = 1; // 다음 키워드는 처음부터
		articleBufferIterator = null;
	}
}
