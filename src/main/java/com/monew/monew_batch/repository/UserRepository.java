package com.monew.monew_batch.repository;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.monew.monew_batch.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	@Modifying
	@Transactional
	@Query("DELETE FROM User u WHERE u.deletedAt < :threshold")
	int deleteByDeletedAtBefore(@Param("threshold") Instant threshold);
}
