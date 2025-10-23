package com.monew.monew_batch.article.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.monew.monew_batch.article.hankyung.dto.HankyungArticleResponse;
import com.monew.monew_batch.article.naver.dto.NaverArticleResponse;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

@Mapper(componentModel = "spring", imports = {com.monew.monew_batch.article.util.DataTimeParser.class})
public interface ArticleMapper {

	@Mapping(target = "source", constant = "NAVER")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", expression = "java(DataTimeParser.parseDateToInstant(item.pubDate))")
	@Mapping(target = "summary", source = "item.description")
	ArticleSaveDto toArticleSaveDto(NaverArticleResponse.NewsItem item);

	@Mapping(target = "source", constant = "HANKYUNG")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", source = "item.pubDate")
	@Mapping(target = "summary", constant = "")
	ArticleSaveDto toArticleSaveDto(HankyungArticleResponse.Item item);
}
