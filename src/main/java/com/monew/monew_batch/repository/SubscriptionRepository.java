package com.monew.monew_batch.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monew.monew_batch.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
