package com.monew.monew_batch.repository;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.monew.monew_batch.entity.Notification;

public interface NotificationsRepository extends JpaRepository<Notification, UUID> {

	@Transactional
	@Modifying
	@Query("DELETE FROM Notification n WHERE n.createdAt <= :threshold")
	int deleteAllOlderThan(@Param("threshold") Instant threshold);
}
