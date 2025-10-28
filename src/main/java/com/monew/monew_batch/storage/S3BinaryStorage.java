package com.monew.monew_batch.storage;

import static com.monew.monew_batch.util.DataTimeParser.*;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.config.AwsConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

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
		String formattedDate = getDateFromInstant(date);
		String key = PATH + formattedDate + "/" + id;

		s3.putObject(b -> b.bucket(config.getBucket())
			.key(key), RequestBody.fromBytes(data));

		return id;
	}

	@Override
	public InputStream get(UUID id, Instant date) {
		String formattedDate = getDateFromInstant(date);
		String key = PATH + formattedDate + "/" + id;

		return s3.getObject(b -> b.bucket(config.getBucket())
			.key(key));
	}

	@Override
	public Boolean exists(UUID id, Instant date) {
		String formattedDate = getDateFromInstant(date);
		String key = PATH + formattedDate + "/" + id;

		try {
			HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
				.bucket(config.getBucket())
				.key(key)
				.build();

			s3.headObject(headObjectRequest);
			return true;
		} catch (NoSuchKeyException e) {
			return false;
		} catch (Exception e) {
			return false;
		}

	}
}
