package com.monew.monew_batch.article;

import java.util.List;

import com.monew.monew_batch.writer.dto.ArticleSaveDto;

public interface ApiArticleService {
	List<ArticleSaveDto> getArticleList(String query, int start, int display, String sort);
	// rss  -> feed만 있으면 된다.
	//      -> 기사 날짜 기준으로 데이터를 수집할 예정
	//      -> 기사 개수가 그렇게 많지 않다.

	// api  -> 키워드, 정렬방식, 검색 시작 위치, 한 번에 표시할 검색 결과 개수
	//      -> 검색을 할 때는 키워드 검색인데(db 데이터)
	//      -> 키워드 추가할 때는
	//      	-> 키워드 존재하면 api를 통해 db에 넣는게 나을 듯
	//       	-> 별개로 키워드마다 이후 1시간씩 스케줄러로 가져옴
	//      -> 배치로 할 때는 날짜순으로 해야하겠다.

}
