package com.monew.monew_batch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.monew.monew_batch.reader.dto.HankyungArticleResponse;
import com.monew.monew_batch.reader.dto.NaverArticleResponse;
import com.monew.monew_batch.util.DataTimeParser;
import com.monew.monew_batch.writer.dto.ArticleSaveDto;

@Mapper(componentModel = "spring", imports = {DataTimeParser.class})
public interface ArticleMapper {

	@Mapping(target = "source", constant = "NAVER")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", expression = "java(DataTimeParser.parseDateToInstant(item.pubDate))")
	@Mapping(target = "summary", source = "item.description")
	ArticleSaveDto toArticleSaveDto(NaverArticleResponse.ArticleItem item);

	@Mapping(target = "source", constant = "HANKYUNG")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", source = "item.pubDate")
	@Mapping(target = "summary", constant = "")
	ArticleSaveDto toArticleSaveDto(HankyungArticleResponse.Item item);
}
