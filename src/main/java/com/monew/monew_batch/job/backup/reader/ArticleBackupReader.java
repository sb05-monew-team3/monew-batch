package com.monew.monew_batch.job.backup.reader;

import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleBackupReader implements ItemReader<Article> {

	private final ArticleRepository articleRepository;

	private Iterator<Article> articleIterator;
	private int currentPage = 0;
	private static final int PAGE_SIZE = 100;
	private boolean isInitialized = false;

	@Override
	public Article read() throws Exception {
		if (!isInitialized) {
			initialize();
		}

		if (articleIterator != null && articleIterator.hasNext()) {
			Article article = articleIterator.next();
			log.debug("[기사 백업] Read: uuid={}, title={}", article.getId(), article.getTitle());
			return article;
		}

		Page<Article> page = articleRepository.findAll(
			PageRequest.of(currentPage++, PAGE_SIZE, Sort.by("id").ascending())
		);

		if (page.hasContent()) {
			articleIterator = page.getContent().iterator();
			return read();
		}

		log.info("[기사 백업] Reader 종료: 모든 데이터 읽기 완료");
		return null;
	}

	private void initialize() {
		log.info("[기사 백업] Reader 초기화 시작");
		this.isInitialized = true;
		this.currentPage = 0;
		this.articleIterator = null;
	}
}