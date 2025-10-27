package com.monew.monew_batch.job.reader;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.job.dto.ArticleSaveDto;
import com.monew.monew_batch.job.reader.dto.HankyungArticleResponse;
import com.monew.monew_batch.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HankyungArticleService {

	private final ArticleMapper articleMapper;
	private RestClient hankyungRestClient;

	/**
	 * 		try {
	 * 			NaverArticleResponse body = naverRestClient.get()
	 * 				.uri(uriBuilder -> uriBuilder
	 * 					.queryParam("query", query)
	 * 					.queryParam("display", display)
	 * 					.queryParam("start", start)
	 * 					.queryParam("sort", sort)
	 * 					.build(false)
	 * 				)
	 * 				.retrieve()
	 * 				.body(NaverArticleResponse.class);
	 *
	 * 			List<ArticleSaveDto> articleSaveDtos = new ArrayList<>();
	 *
	 * 			if (body == null || body.getItems() == null) {
	 * 				return articleSaveDtos;
	 *                        }
	 *
	 * 			body.getItems().forEach(item -> {
	 * 				ArticleSaveDto articleSaveDto = articleMapper.toArticleSaveDto(item);
	 * 				articleSaveDtos.add(articleSaveDto);
	 *            });
	 *
	 * 			return articleSaveDtos;
	 *        * 		} catch (Exception e) {
	 * 			log.error("네이버 뉴스 API 호출 실패", e);
	 * 			throw new RuntimeException("네이버 뉴스 API 호출 실패: " + e.getMessage(), e);        * 		}
	 */

	public List<ArticleSaveDto> getArticleList(String feed) {
		try {
			HankyungArticleResponse body = hankyungRestClient.get()
				.uri("/" + feed)
				.retrieve()
				.body(HankyungArticleResponse.class);

			List<ArticleSaveDto> articleSaveDtos = new ArrayList<>();

			if (body == null || body.getChannel() == null || body.getChannel().getItems() == null) {
				return articleSaveDtos;
			}
			body.getChannel().getItems().forEach(item -> {
				ArticleSaveDto articleSaveDto = articleMapper.toArticleSaveDto(item);
				articleSaveDtos.add(articleSaveDto);
			});

			return articleSaveDtos;
		} catch (Exception e) {
			log.error("한국경제 뉴스 API 호출 실패", e);
			throw new RuntimeException("한국 경제 뉴스 API 호출 실패: " + e.getMessage(), e);
		}
	}
}
