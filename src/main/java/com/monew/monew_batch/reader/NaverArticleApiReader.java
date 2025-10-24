package com.monew.monew_batch.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.mapper.ArticleMapper;
import com.monew.monew_batch.reader.client.NaverApiClient;
import com.monew.monew_batch.repository.InterestKeywordRepository;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverArticleApiReader implements ItemStreamReader<ArticleSaveDto> {

	private final ArticleMapper articleMapper;
	private final NaverApiClient naverApiClient;
	private final InterestKeywordRepository interestKeywordRepository;

	private String currentKeyword;
	private static final String SORT = "sim";
	private static final int DISPLAY = 100;
	private int currentStart = 1;
	private Iterator<ArticleSaveDto> articleIterator;
	private Iterator<String> keywordIterator;

	@Override
	public ArticleSaveDto read() {
		if (keywordIterator == null) {
			List<String> distinctNames = interestKeywordRepository.findDistinctNames();

			if (distinctNames == null || distinctNames.isEmpty()) {
				log.info("[주기 배치 작업] 키워드 값이 존재하지 않음");
				return null;
			}
			keywordIterator = distinctNames.iterator();
		}

		if (articleIterator == null || !articleIterator.hasNext()) {
			if (!keywordIterator.hasNext()) {
				log.info("[주기 배치 작업] 모든 키워드 기사 수집 완료(100개 단위)");
				return null;
			}
			log.info("[주기 배치 작업]네이버 뉴스 API 호출: current page = {}", currentStart);

			currentKeyword = keywordIterator.next();
			currentStart = 1;

			log.info("[주기 배치 작업] 현재 키워드 '{}'", currentKeyword);
			List<ArticleSaveDto> articles = naverApiClient.fetchArticles(
				currentKeyword,
				DISPLAY,
				currentStart,
				SORT
			);

			if (articles == null || articles.isEmpty()) {
				log.info("[주기 배치 작업] 키워드 '{}' 관련 기사 없음", currentKeyword);
				return read();
			}
			articleIterator = articles.iterator();
			currentStart++;
		}
		return articleIterator.next();
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		ItemStreamReader.super.open(executionContext);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		ItemStreamReader.super.update(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		ItemStreamReader.super.close();
	}

}
