package com.monew.monew_batch.storage;

import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.config.AwsConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class S3BinaryStorage implements BinaryStorage {

	private final AwsConfig config;
	private final S3Client s3;
	private static final String PATH = "article/";

	@Override
	public UUID put(UUID id, Instant date, byte[] data) {
		String formattedDate = getDate(date);
		String key = PATH + formattedDate + "/" + id;

		s3.putObject(b -> b.bucket(config.getBucket())
			.key(key), RequestBody.fromBytes(data));

		return id;
	}

	@Override
	public InputStream get(UUID id, Instant date) {
		String formattedDate = getDate(date);
		String key = PATH + formattedDate + "/" + id;

		return s3.getObject(b -> b.bucket(config.getBucket())
			.key(key));
	}

	private String getDate(Instant date) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd")
			.withZone(ZoneId.of("Asia/Seoul"))
			.format(date);
	}
}
