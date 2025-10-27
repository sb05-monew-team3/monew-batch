package com.monew.monew_batch.job.reader.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "rss")
public class HankyungArticleResponse {
	@JacksonXmlProperty(isAttribute = true, localName = "version")
	private String version; // "2.0"

	@JacksonXmlProperty(localName = "channel")
	private Channel channel;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Channel {
		@JacksonXmlProperty(localName = "title")
		private String title;              // <![CDATA[ 한국경제 | 뉴스 | 경제 ]]>

		@JacksonXmlProperty(localName = "link")
		private String link;               // https://www.hankyung.com/all-news-economy

		@JacksonXmlProperty(localName = "language")
		private String language;           // ko

		@JacksonXmlProperty(localName = "copyright")
		private String copyright;

		@JacksonXmlProperty(localName = "lastBuildDate")
		private String lastBuildDate;      // Thu, 23 Oct 2025 14:11:31 +0900

		@JacksonXmlProperty(localName = "description")
		private String description;

		// item은 래퍼 태그 없이 반복됨
		@JacksonXmlElementWrapper(useWrapping = false)
		@JacksonXmlProperty(localName = "item")
		private List<Item> items;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		@JacksonXmlProperty(localName = "title")
		private String title;      // <![CDATA[ ... ]]>

		@JacksonXmlProperty(localName = "link")
		private String link;       // <![CDATA[ https://www.hankyung.com/article/... ]]>

		@JacksonXmlProperty(localName = "author")
		private String author;     // <![CDATA[ 기자명 ]]>

		@JacksonXmlProperty(localName = "pubDate")
		private String pubDate;    // Thu, 23 Oct 2025 13:46:33 +0900
	}
}
