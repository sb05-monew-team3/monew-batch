package com.monew.monew_batch.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.monew.monew_batch.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	@Query("SELECT u "
		+ "FROM User u "
		+ "WHERE u.deletedAt IS NOT NULL "
		+ "AND u.deletedAt < :threshold")
	List<User> findDeletedUsers(@Param("threshold") Instant threshold);
}
