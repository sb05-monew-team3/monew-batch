package com.monew.monew_batch.schedule;

import com.monew.monew_batch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

	private final UserRepository userRepository;

	/**
	 * 1분마다 deleted_at이 채워진 사용자들을 삭제
	 */
	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void cleanupDeletedUsers() {
		log.info("[UserCleanupScheduler] 삭제 스케줄러 실행 시작");

		try {
			long count = userRepository.deleteUsersMarkedAsDeleted();
			log.info("[UserCleanupScheduler] 삭제된 사용자 수: {}", count);
		} catch (Exception e) {
			log.error("[UserCleanupScheduler] 삭제 중 오류 발생: {}", e.getMessage(), e);
		}

		log.info("[UserCleanupScheduler] 삭제 스케줄러 실행 종료");
	}
}
