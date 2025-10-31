package com.monew.monew_batch.job.rss_article_collection.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "rss")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChosunArticleResponse {

	@JacksonXmlProperty(isAttribute = true, localName = "version")
	private String version;

	@JacksonXmlProperty(localName = "channel")
	private Channel channel;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Channel {
		@JacksonXmlProperty(localName = "title")
		private String title;

		@JacksonXmlProperty(localName = "link")
		private String link;

		@JacksonXmlProperty(localName = "description")
		private String description;

		@JacksonXmlProperty(localName = "lastBuildDate")
		private String lastBuildDate;

		@JacksonXmlProperty(localName = "language")
		private String language;

		@JacksonXmlProperty(localName = "category")
		private String category;

		@JacksonXmlProperty(localName = "ttl")
		private String ttl;

		@JacksonXmlProperty(localName = "item")
		@JacksonXmlElementWrapper(useWrapping = false)
		private List<Item> items;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		@JacksonXmlProperty(localName = "title")
		private String title;

		@JacksonXmlProperty(localName = "link")
		private String link;

		@JacksonXmlProperty(localName = "guid")
		private Guid guid;

		@JacksonXmlProperty(localName = "creator", namespace = "http://purl.org/dc/elements/1.1/")
		private String creator;

		@JacksonXmlProperty(localName = "description")
		private String description;

		@JacksonXmlProperty(localName = "pubDate")
		private String pubDate;

		@JacksonXmlProperty(localName = "encoded", namespace = "http://purl.org/rss/1.0/modules/content/")
		private String contentEncoded;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)  // ← 알 수 없는 필드 무시
	public static class Guid {
		@JacksonXmlProperty(isAttribute = true, localName = "isPermaLink")
		private String isPermaLink;

		@JacksonXmlProperty(localName = "")
		private String value;
	}
}