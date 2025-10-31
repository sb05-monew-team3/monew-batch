package com.monew.monew_batch.job.rss_article_collection.dto;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "rss")
public class HankyungArticleResponse {

	@JacksonXmlProperty(isAttribute = true, localName = "version")
	private String version;

	@JacksonXmlProperty(localName = "channel")
	private Channel channel;

	@Data
	public static class Channel {
		@JacksonXmlProperty(localName = "title")
		private String title;

		@JacksonXmlProperty(localName = "link")
		private String link;

		@JacksonXmlProperty(localName = "language")
		private String language;

		@JacksonXmlProperty(localName = "copyright")
		private String copyright;

		@JacksonXmlProperty(localName = "lastBuildDate")
		private String lastBuildDate;

		@JacksonXmlProperty(localName = "description")
		private String description;

		@JacksonXmlProperty(localName = "item")
		@JacksonXmlElementWrapper(useWrapping = false)
		private List<Item> items;
	}

	@Data
	public static class Item {
		@JacksonXmlProperty(localName = "title")
		private String title;

		@JacksonXmlProperty(localName = "link")
		private String link;

		@JacksonXmlProperty(localName = "author")
		private String author;

		@JacksonXmlProperty(localName = "pubDate")
		private String pubDate;
	}
}