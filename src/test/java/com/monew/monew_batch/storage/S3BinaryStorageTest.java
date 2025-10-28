package com.monew.monew_batch.storage;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monew.monew_batch.config.AwsConfig;
import com.monew.monew_batch.entity.Article;
import com.monew.monew_batch.entity.ArticleSource;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class S3ArticleStorageTest {

	@Autowired
	private BinaryStorage binaryStorage;

	@Autowired
	private AwsConfig config;

	@Autowired
	private S3Client s3;

	private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	private UUID savedId;
	private Instant savedDate;
	private String savedKey;

	@BeforeEach
	void setUp() {
		savedId = UUID.randomUUID();
		savedDate = Instant.now();
	}

	@Test
	@Order(1)
	@DisplayName("S3 업로드 및 다운로드(byte[]) 테스트")
	void putAndGetArticleTest() throws Exception {
		// given
		Article article = Article.builder()
			.title("test")
			.publishDate(savedDate)
			.source(ArticleSource.NAVER)
			.summary("summary test")
			.build();

		byte[] data = objectMapper.writeValueAsBytes(article);

		// when
		UUID id = binaryStorage.put(savedId, savedDate, data);

		// then
		InputStream stream = binaryStorage.get(id, savedDate);
		byte[] downloaded = readAllBytes(stream);

		assertThat(downloaded).isEqualTo(data);

		// S3 key 저장 : 데이터 삭제 위함
		savedKey = "article/" +
			DateTimeFormatter.ofPattern("yyyy-MM-dd")
				.withZone(ZoneId.of("Asia/Seoul"))
				.format(savedDate) +
			"/" + id;
	}

	@AfterEach
	void cleanUp() {
		if (savedKey != null) {
			try {
				s3.deleteObject(DeleteObjectRequest.builder()
					.bucket(config.getBucket())
					.key(savedKey)
					.build());
				System.out.println("테스트 종료 후 S3 객체 삭제 완료: " + savedKey);
			} catch (Exception e) {
				System.err.println("S3 객체 삭제 실패: " + e.getMessage());
			}
		}
	}

	// InputStream -> byte[] 변환
	private byte[] readAllBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] temp = new byte[4096];
		int n;
		while ((n = inputStream.read(temp)) != -1) {
			buffer.write(temp, 0, n);
		}
		return buffer.toByteArray();
	}
}
