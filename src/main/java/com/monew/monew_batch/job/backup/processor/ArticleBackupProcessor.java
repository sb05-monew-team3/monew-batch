package com.monew.monew_batch.job.backup.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.entity.Interest;
import com.monew.monew_batch.job.backup.dto.ArticleBackUpDto;
import com.monew.monew_batch.repository.InterestRepository;
import com.monew.monew_batch.storage.BinaryStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleBackupProcessor implements ItemProcessor<Article, ArticleBackUpDto> {

	private final BinaryStorage binaryStorage;
	private final InterestRepository interestRepository;
	private Iterator<ArticleBackUpDto> iterator;

	@Override
	public ArticleBackUpDto process(Article article) {

		if (iterator != null && iterator.hasNext()) {
			return iterator.next();
		}

		UUID articleId = article.getId();
		List<Interest> interests = interestRepository.findByArticleId(articleId);
		List<ArticleBackUpDto> dtoList = new ArrayList<>();

		for (Interest interest : interests) {
			String interestName = interest.getName();

			if (binaryStorage.exists(articleId, interestName, article.getPublishDate())) {
				log.info("[기사 백업] 이미 존재함(건너뜀): uuid={}, interestName={}, title={}",
					articleId, interestName, article.getTitle());
				continue;
			}

			ArticleBackUpDto dto = ArticleBackUpDto.builder()
				.article(article)
				.interestName(interestName)
				.build();

			dtoList.add(dto);
			log.debug("[기사 백업] 처리 중: uuid={}, interestName={}, title={}",
				articleId, interestName, article.getTitle());
		}

		if (dtoList.isEmpty()) {
			iterator = null;
			return null;
		}

		iterator = dtoList.iterator();
		return iterator.next();
	}
}