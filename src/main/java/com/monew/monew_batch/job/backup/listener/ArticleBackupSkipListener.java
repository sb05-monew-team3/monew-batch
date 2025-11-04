package com.monew.monew_batch.job.backup.listener;

import org.springframework.batch.core.SkipListener;

import com.monew.monew_batch.entity.Article;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArticleBackupSkipListener implements SkipListener<Article, Article> {

	@Override
	public void onSkipInWrite(Article item, Throwable t) {
		log.error("[기사 백업] 쓰기 실패 건 스킵됨: id={}, title={}, error={}", item.getId(), item.getTitle(), t.getMessage());
	}

	@Override
	public void onSkipInProcess(Article item, Throwable t) {
		log.error("[기사 백업] 처리 실패 건 스킵됨: id={}, title={}, error={}", item.getId(), item.getTitle(), t.getMessage());
	}

	@Override
	public void onSkipInRead(Throwable t) {
		log.error("[기사 백업] 읽기 실패 스킵됨: {}", t.getMessage());
	}
}