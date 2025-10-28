package com.monew.monew_batch.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.monew.monew_batch.properties.ChosunArticleProperties;
import com.monew.monew_batch.properties.HankyungArticleProperties;
import com.monew.monew_batch.properties.NaverArticleApiProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

	private final NaverArticleApiProperties naverArticleApiProperties;
	private final HankyungArticleProperties hankyungArticleProperties;
	private final ChosunArticleProperties chosunArticleProperties;

	@Bean
	public RestClient naverRestClient() {
		return RestClient.builder()
			.baseUrl(naverArticleApiProperties.getBaseUrl())
			.defaultHeader("X-Naver-Client-Id", naverArticleApiProperties.getClientId())
			.defaultHeader("X-Naver-Client-Secret", naverArticleApiProperties.getClientSecret())
			.defaultHeader("Accept", "application/json")
			.defaultHeader("User-Agent", "Mozilla/5.0")
			.build();
	}

	@Bean
	public RestClient hankyungRestClient() {
		XmlMapper xmlMapper = new XmlMapper();

		MappingJackson2XmlHttpMessageConverter xmlConverter =
			new MappingJackson2XmlHttpMessageConverter(xmlMapper);

		xmlConverter.setSupportedMediaTypes(Arrays.asList(
			MediaType.APPLICATION_XML,
			MediaType.TEXT_XML,
			new MediaType("application", "rss+xml"),
			new MediaType("text", "xml")
		));

		return RestClient.builder()
			.baseUrl(hankyungArticleProperties.getBaseUrl())
			.defaultHeader("User-Agent", "Mozilla/5.0")
			.messageConverters(converters -> {
				converters.add(0, xmlConverter);
			})
			.build();
	}

	@Bean
	public RestClient chosunRestClient() {
		XmlMapper xmlMapper = new XmlMapper();

		MappingJackson2XmlHttpMessageConverter xmlConverter =
			new MappingJackson2XmlHttpMessageConverter(xmlMapper);

		xmlConverter.setSupportedMediaTypes(Arrays.asList(
			MediaType.APPLICATION_XML,
			MediaType.TEXT_XML,
			MediaType.TEXT_HTML,
			new MediaType("application", "rss+xml"),
			new MediaType("text", "xml")
		));

		return RestClient.builder()
			.baseUrl(chosunArticleProperties.getBaseUrl())
			.defaultHeader("User-Agent", "Mozilla/5.0")
			.messageConverters(converters -> {
				converters.add(0, xmlConverter);
			})
			.build();
	}
}
