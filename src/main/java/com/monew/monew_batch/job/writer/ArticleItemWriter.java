package com.monew.monew_batch.job.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.job.dto.ArticleSaveDto;
import com.monew.monew_batch.mapper.ArticleMapper;
import com.monew.monew_batch.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArticleItemWriter implements ItemWriter<ArticleSaveDto> {

	private final ArticleRepository articleRepository;
	private final ArticleMapper articleMapper;

	@Override
	public void write(Chunk<? extends ArticleSaveDto> items) throws Exception {
		if (items == null || items.isEmpty()) {
			return;
		}

		List<Article> articles = new ArrayList<>();

		for (ArticleSaveDto item : items) {
			Article entity = articleMapper.toEntity(item);
			articles.add(entity);
		}

		articleRepository.saveAll(articles);
	}
}
