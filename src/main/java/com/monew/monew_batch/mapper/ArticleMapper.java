package com.monew.monew_batch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.entity.ArticleSource;
import com.monew.monew_batch.job.dto.ArticleSaveDto;
import com.monew.monew_batch.job.reader.dto.HankyungArticleResponse;
import com.monew.monew_batch.job.reader.dto.NaverArticleResponse;
import com.monew.monew_batch.util.DataTimeParser;

@Mapper(componentModel = "spring", imports = {ArticleSource.class, DataTimeParser.class})
public interface ArticleMapper {

	@Mapping(target = "source", expression = "java(ArticleSource.NAVER)")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", expression = "java(DataTimeParser.parseDateToInstant(item.getPubDate()))")
	@Mapping(target = "summary", source = "item.description")
	ArticleSaveDto toArticleSaveDto(NaverArticleResponse.ArticleItem item);

	@Mapping(target = "source", expression = "java(ArticleSource.HANKYUNG)")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", source = "item.pubDate")
	@Mapping(target = "summary", constant = "")
	ArticleSaveDto toArticleSaveDto(HankyungArticleResponse.Item item);

	Article toEntity(ArticleSaveDto dto);
}
