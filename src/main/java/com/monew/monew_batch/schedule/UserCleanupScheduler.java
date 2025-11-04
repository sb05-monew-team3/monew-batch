package com.monew.monew_batch.schedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

	private final UserRepository userRepository;

	@Scheduled(cron = "0 0 1 * * *")
	public void cleanupDeletedUsers() {
		log.info("[UserCleanupScheduler] 삭제 스케줄러 실행 시작");

		try {
			Instant threshold = Instant.now().minus(1, ChronoUnit.DAYS);
			int i = userRepository.deleteByDeletedAtBefore(threshold);
			log.info("[UserCleanupScheduler] 삭제 완료 : {}", i);
		} catch (Exception e) {
			log.error("[UserCleanupScheduler] 삭제 중 오류 발생: {}", e.getMessage(), e);
		}

		log.info("[UserCleanupScheduler] 삭제 스케줄러 실행 종료");
	}
}
