package com.monew.monew_batch.job.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Service;

import com.monew.monew_batch.job.dto.ArticleSaveDto;
import com.monew.monew_batch.job.reader.client.HankyungRssClient;
import com.monew.monew_batch.properties.HankyungArticleProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HankyungArticleRssReader implements ItemStreamReader<ArticleSaveDto> {

	private final HankyungRssClient hankyungRssClient;
	private final HankyungArticleProperties hankyungArticleProperties;

	private int feedIndex;
	private List<String> feeds;
	private Iterator<ArticleSaveDto> articleBufferIterator;

	@Override
	public void open(ExecutionContext executionContext) {
		feedIndex = 0;
		feeds = hankyungArticleProperties.getFeeds();
	}

	@Override
	public ArticleSaveDto read() throws Exception {
		if (feedIndex >= feeds.size()) {
			log.info("[한국경제] 모든 feed 대상 수집 완료");
			feedIndex = 0;
			return null;
		}

		if (articleBufferIterator == null || !articleBufferIterator.hasNext()) {
			List<ArticleSaveDto> articleSaveDtos = hankyungRssClient.fetchArticles(feeds.get(feedIndex++));
			log.info("[한국경제] 키워드 {}를 대상으로 수집 완료, 기사 개수 {}", feeds.get(feedIndex - 1), articleSaveDtos.size());
			articleBufferIterator = articleSaveDtos.iterator();
		}
		return articleBufferIterator.next();
	}
}
