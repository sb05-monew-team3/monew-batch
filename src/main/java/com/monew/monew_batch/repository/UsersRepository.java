package com.monew.monew_batch.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monew.monew_batch.entity.User;

public interface UsersRepository extends JpaRepository<User, UUID> {

	List<User> findAllByDeletedAtIsNull();
}
