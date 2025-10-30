package com.monew.monew_batch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
	// @Scheduled 어노테이션이 작동하도록 스케쥴링 기능을 활성화
}
