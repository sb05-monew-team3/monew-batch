package com.monew.monew_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.monew.monew_batch.article.hankyung.HankyungArticleProperties;
import com.monew.monew_batch.article.naver.NaverApiProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({NaverApiProperties.class, HankyungArticleProperties.class})
public class MonewBatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonewBatchApplication.class, args);
		System.out.println("localhost:8082");
	}

}
