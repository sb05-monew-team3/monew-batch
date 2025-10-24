package com.monew.monew_batch.reader;

import java.util.Iterator;
import java.util.List;

import com.monew.monew_batch.reader.dto.NaverArticleResponse;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.reader.client.NaverApiClient;
import com.monew.monew_batch.repository.InterestKeywordRepository;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverArticleApiReader implements ItemStreamReader<ArticleSaveDto> {

    private final NaverApiClient naverApiClient;
    private final InterestKeywordRepository interestKeywordRepository;

    private static final String SORT = "sim";
    private static final int DISPLAY = 30;

    private List<String> keywords;
    private Iterator<ArticleSaveDto> articleIterator;
    private int currentPage = 1; // 공통 페이지
    private int keywordIndex = 0; // 현재 키워드 위치

    @Override
    public void open(ExecutionContext executionContext) {
        if (executionContext.containsKey("currentPage")) {
            currentPage = executionContext.getInt("currentPage");
        } else {
            currentPage = 1;
        }
        log.info("[네이버 기사 주기 작업] Reader open(): currentPage={}", currentPage);
    }

    @Override
    public ArticleSaveDto read() {
        if (keywords == null) {
            keywords = interestKeywordRepository.findDistinctNames();
            if (keywords == null || keywords.isEmpty()) {
                log.info("[네이버 기사 주기 작업] 키워드 없음, 종료");
                return null;
            }
            log.info("[네이버 기사 주기 작업] 키워드 목록 로드 완료: {}", keywords);
        }

        while (articleIterator == null || !articleIterator.hasNext()) {

            if (keywordIndex >= keywords.size()) {
                log.info("[네이버 기사 주기 작업] 모든 키워드 {}페이지 처리 완료", currentPage);
                currentPage++;
                keywordIndex = 0;
                return null;
            }

            String keyword = keywords.get(keywordIndex++);
            log.info("[네이버 기사 주기 작업] '{}' 키워드 {}페이지 기사 시작", keyword, currentPage);

            try {
                // 키워드로 30개의 기사를 읽어옴
                List<ArticleSaveDto> articles = naverApiClient.fetchArticles(keyword, DISPLAY, currentPage, SORT);

                if (articles == null || articles.isEmpty()) {
                    log.info("[네이버 기사 주기 작업] '{}' 키워드 기사 없음", keyword);
                    continue;
                }

                articleIterator = articles.iterator();
            } catch (Exception e) {
                log.error("[네이버 기사 주기 작업] '{}' 키워드 {}페이지 수집 중 오류 발생: {}", keyword, currentPage, e.getMessage());
                throw new RuntimeException("API 호출 실패 - 전체 페이지 재시도 필요", e);
            }
        }
        return articleIterator.next();
    }

    @Override
    public void update(ExecutionContext executionContext) {
        executionContext.putInt("currentPage", currentPage);
        log.info("[네이버 기사 주기 작업] Reader update(): currentPage={}", currentPage);
    }

    @Override
    public void close() {
        log.info("[네이버 기사 주기 작업] Reader close()");
    }
}
