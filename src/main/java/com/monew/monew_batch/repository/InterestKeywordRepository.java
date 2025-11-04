package com.monew.monew_batch.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.monew.monew_batch.entity.InterestKeyword;

public interface InterestKeywordRepository extends JpaRepository<InterestKeyword, UUID> {

	@Query("SELECT DISTINCT i.name FROM InterestKeyword i")
	List<String> findDistinctNames();

	List<InterestKeyword> findByName(String name);
}
