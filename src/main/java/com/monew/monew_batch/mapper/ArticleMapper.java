package com.monew.monew_batch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.entity.ArticleSource;
import com.monew.monew_batch.job.api_article_collection.dto.NaverArticleResponse;
import com.monew.monew_batch.job.common.dto.ArticleSaveDto;
import com.monew.monew_batch.job.rss_article_collection.dto.ChosunArticleResponse;
import com.monew.monew_batch.job.rss_article_collection.dto.HankyungArticleResponse;
import com.monew.monew_batch.job.rss_article_collection.dto.YonhapArticleResponse;
import com.monew.monew_batch.util.DataTimeParser;
import com.monew.monew_batch.util.HtmlParser;

@Mapper(componentModel = "spring", imports = {ArticleSource.class, DataTimeParser.class, HtmlParser.class})
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
	@Mapping(target = "publishDate", expression = "java(DataTimeParser.parseDateToInstant(item.getPubDate()))")
	@Mapping(target = "summary", constant = "")
	ArticleSaveDto toArticleSaveDto(HankyungArticleResponse.Item item);

	@Mapping(target = "source", expression = "java(ArticleSource.CHOSUN)")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", expression = "java(DataTimeParser.parseDateToInstant(item.getPubDate()))")
	@Mapping(target = "summary", expression = "java(HtmlParser.extractLastParagraph(item.getContentEncoded()))")
	ArticleSaveDto toArticleSaveDto(ChosunArticleResponse.Item item);

	@Mapping(target = "source", expression = "java(ArticleSource.YEONHAP)")
	@Mapping(target = "sourceUrl", source = "item.link")
	@Mapping(target = "title", source = "item.title")
	@Mapping(target = "publishDate", expression = "java(DataTimeParser.parseDateToInstant(item.getPubDate()))")
	@Mapping(target = "summary", source = "item.description")
	ArticleSaveDto toArticleSaveDto(YonhapArticleResponse.Item item);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	Article toEntity(ArticleSaveDto dto);
}
