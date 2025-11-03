package com.monew.monew_batch.repository;

import com.monew.monew_batch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM users WHERE deleted_at IS NOT NULL", nativeQuery = true)
	int deleteUsersMarkedAsDeleted();
}
