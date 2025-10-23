package com.monew.monew_batch.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monew.monew_batch.entity.InterestKeyword;

public interface InterestKeywordRepository extends JpaRepository<InterestKeyword, UUID> {

	// distinct하게 name들만 가져오기
}
