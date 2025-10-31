package com.monew.monew_batch.job.rss_article_collection.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.job.rss_article_collection.client.ChosunRssClient;
import com.monew.monew_batch.properties.ChosunArticleProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 디렉터리
 * 하나의 job에 bean에 다 등록하지말것
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ChosunArticleRssReader implements ItemStreamReader<ArticleSaveDto> {

	private final ChosunRssClient chosunRssClient;
	private final ChosunArticleProperties chosunArticleProperties;

	private int feedIndex;
	private List<String> feeds;
	private Iterator<ArticleSaveDto> articleBufferIterator;

	@Override
	public void open(ExecutionContext executionContext) {
		feedIndex = 0;
		feeds = chosunArticleProperties.getFeeds();
	}

	@Override
	public ArticleSaveDto read() {
		if (feedIndex >= feeds.size()) {
			log.info("[조선뉴스] 모든 feed 대상 수집 완료");
			feedIndex = 0;
			return null;
		}

		if (articleBufferIterator == null || !articleBufferIterator.hasNext()) {
			List<ArticleSaveDto> articleSaveDtos = chosunRssClient.fetchArticles(feeds.get(feedIndex++));
			log.info("[조선뉴스] 키워드 {}를 대상으로 수집 완료, 기사 개수 {}", feeds.get(feedIndex - 1), articleSaveDtos.size());
			articleBufferIterator = articleSaveDtos.iterator();
		}

		return articleBufferIterator.next();
	}

}
