package com.monew.monew_batch.schedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.monew.monew_batch.entity.User;
import com.monew.monew_batch.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

	private final UserRepository userRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private Environment environment;

	// 환경변수(API_SERVER_URL) 가 없을 경우 localhost로 fallback
	@Value("${API_SERVER_URL:http://localhost:8080}")
	private String apiBaseUrl;

	/**
	 * 매 정시마다 논리 삭제된 사용자 중 1일 이상 경과한 계정 자동 하드 삭제
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void deleteExpiredUsers() {
		Instant threshold = Instant.now().minus(1, ChronoUnit.DAYS);
		List<User> expiredUsers = userRepository.findDeletedUsers(threshold);

		if (expiredUsers.isEmpty()) {
			log.info("[UserCleanupScheduler] 삭제 대상 없음 (현재 시간: {})", Instant.now());
			return;
		}

		// 현재 실행 프로필 확인 (local / dev / prod)
		String profile = getActiveProfile();
		String baseUrl = resolveBaseUrl(profile);

		log.info("[UserCleanupScheduler] 활성 프로필 : {}, API 서버: {}", profile, baseUrl);

		for (User user : expiredUsers) {
			try {
				String url = baseUrl + "/api/users/" + user.getId() + "/hard";
				restTemplate.delete(url);
				log.info("[UserCleanupScheduler] 사용자 {} 하드 삭제 성공", user.getId());
			} catch (Exception e) {
				log.error("[UserCleanupScheduler] 사용자 {} 하드 삭제 실패: {}", user.getId(), e.getMessage());
			}
		}
	}

	/**
	 *  현재 활성화된 Spring Profile 반환 (없으면 local)
	 */
	private String getActiveProfile() {
		String[] profiles = environment.getActiveProfiles();
		if (profiles.length == 0) {
			return "local";
		}
		return profiles[0];
	}

	/**
	 * 환경에 따라 호출할 base URL 결정
	 */
	private String resolveBaseUrl(String profile) {
		String url = apiBaseUrl;

		if ("local".equals(profile)){
			url = "http://localhost:8080";
		}

		// 환경변수가 존재하지 않거나 비어 있다면 fallback
		if (url == null || url.isBlank()) {
			log.warn("API_SERVER_URL 환경변수가 설정되지 않아 기본값 localhost로 대체됩니다.");
			url = "http://localhost:8080";
		}

		return  url;
	}
}
