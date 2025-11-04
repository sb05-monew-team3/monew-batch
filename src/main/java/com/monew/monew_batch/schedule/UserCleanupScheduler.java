package com.monew.monew_batch.schedule;

import org.springframework.stereotype.Component;

import com.monew.monew_batch.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

	private final UserRepository userRepository;

	// @Scheduled(cron = "0 0 0 * * *")
	public void cleanupDeletedUsers() {
		log.info("[UserCleanupScheduler] 삭제 스케줄러 실행 시작");

		try {
			long count = userRepository.deleteByDeletedAtIsNotNull();
			log.info("[UserCleanupScheduler] 삭제된 사용자 수: {}", count);
		} catch (Exception e) {
			log.error("[UserCleanupScheduler] 삭제 중 오류 발생: {}", e.getMessage(), e);
		}

		log.info("[UserCleanupScheduler] 삭제 스케줄러 실행 종료");
	}
}
