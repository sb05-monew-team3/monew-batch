package com.monew.monew_batch.reader.client;

import java.util.ArrayList;
import java.util.List;

import com.monew.monew_batch.mapper.ArticleMapper;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.monew.monew_batch.reader.dto.NaverArticleResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverApiClient {

	private final RestClient naverRestClient;
    private final ArticleMapper articleMapper;

    /**
	 *
	 * @param query : 키워드
	 * @param display : 한 페이지 수
	 * @param start : 페이지 수
	 * @param sort : 정렬 기준 "sim: 정확도 내림차", "date: 날짜순 내림차 "
	 * @return
	 */
	public List<ArticleSaveDto> fetchArticles(String query, int display, int start, String sort) {
		try {
			NaverArticleResponse naverArticleResponse = naverRestClient.get()
				.uri(uriBuilder -> uriBuilder
					.queryParam("query", query)
					.queryParam("display", display)
					.queryParam("start", start)
					.queryParam("sort", sort)
					.build(false)
				)
				.retrieve()
				.body(NaverArticleResponse.class);

            if(naverArticleResponse == null){
                return null;
            }

            List<NaverArticleResponse.ArticleItem> items = naverArticleResponse.getItems();

            List<ArticleSaveDto> returnValue = new ArrayList<>();
            if(items == null || items.isEmpty()){
                return null;
            }else{
                for(NaverArticleResponse.ArticleItem item : items){
                    ArticleSaveDto articleSaveDto = articleMapper.toArticleSaveDto(item);
                    returnValue.add(articleSaveDto);
                }
            }
            return  returnValue;
        } catch (Exception e) {
			log.error("네이버 뉴스 API 호출 실패", e);
			throw new RuntimeException("네이버 뉴스 API 호출 실패: " + e.getMessage(), e);
		}
	}

}
