package com.monew.monew_batch.job.api_article_collection.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.monew.monew_batch.entity.Interest;
import com.monew.monew_batch.entity.Notification;
import com.monew.monew_batch.entity.NotificationResourceType;
import com.monew.monew_batch.entity.Subscription;
import com.monew.monew_batch.entity.User;
import com.monew.monew_batch.repository.InterestRepository;
import com.monew.monew_batch.repository.NotificationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationWriter implements ItemWriter<Interest> {

	private final NotificationsRepository notificationsRepository;
	private final InterestRepository interestRepository;

	@Override
	public void write(Chunk<? extends Interest> interests) {
		if (interests == null || interests.isEmpty()) {
			log.debug("[네이버 기사 알림 추가] 저장할 알림이 없습니다.");
			return;
		}

		List<Notification> allNotifications = new ArrayList<>();

		for (Interest interest : interests) {
			Interest fullInterest = interestRepository.findByIdWithSubscriptions(interest.getId()).orElseThrow();

			int articleCount = interest.getArticleCount();
			if (articleCount == 0)
				continue;

			for (Subscription sub : fullInterest.getSubscriptions()) {
				User user = sub.getUser();

				if (user == null) {
					log.warn("[네이버 기사 알림 추가] 구독 정보에 사용자가 없음 - Subscription ID: {}", sub.getId());
					continue;
				}

				String message = String.format("%s와 관련된 기사가 %d건 등록되었습니다.",
					interest.getName(), articleCount);

				allNotifications.add(Notification.builder()
					.content(message)
					.user(user)
					.resourceType(NotificationResourceType.interest)
					.resourceId(interest.getId())
					.build());
			}

			log.info("[네이버 기사 알림 추가] 관심사 '{}'에 대해 {}명의 구독자에게 알림 생성",
				interest.getName(), fullInterest.getSubscriptions().size());
		}

		try {
			notificationsRepository.saveAll(allNotifications);
			log.info("[네이버 기사 알림 추가] 총 {}건의 알림 저장 완료", allNotifications.size());
		} catch (Exception e) {
			log.error("[네이버 기사 알림 추가] 알림 저장 실패 - 대상: {}건", allNotifications.size(), e);
			throw e;
		}
	}
}