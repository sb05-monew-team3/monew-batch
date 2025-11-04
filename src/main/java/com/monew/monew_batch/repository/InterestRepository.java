package com.monew.monew_batch.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.monew.monew_batch.entity.Interest;

public interface InterestRepository extends JpaRepository<Interest, UUID> {
	@EntityGraph(attributePaths = {"interestKeywords"})
	@Query("select i from Interest i where i.id = :id")
	Optional<Interest> findByIdWithKeywords(@Param("id") UUID id);

	@EntityGraph(attributePaths = {"subscriptions", "subscriptions.user"})
	@Query("select i from Interest i where i.id = :id")
	Optional<Interest> findByIdWithSubscriptions(@Param("id") UUID id);

	@Query("""
				SELECT i
				FROM Interest i
				JOIN ArticleInterest ai ON ai.interest.id = i.id
				WHERE ai.article.id = :articleId
		""")
	List<Interest> findByArticleId(@Param("articleId") UUID articleId);
}
