package com.monew.monew_batch.job;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.monew.monew_batch.job.listener.JobProcessedCountListener;
import com.monew.monew_batch.repository.NotificationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationDeleteJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final NotificationsRepository notificationsRepository;

	private final JobProcessedCountListener jobProcessedCountListener;

	@Bean
	public Step notificationDeleteStep() {
		return new StepBuilder("deleteOldNotificationsStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				Instant threshold = Instant.now().minus(7, ChronoUnit.DAYS);
				int deletedCount = notificationsRepository.deleteAllOlderThan(threshold);
				log.info("[알림삭제] 7일 이상 지난 알림 {}개 삭제 완료", deletedCount);

				chunkContext.getStepContext()
					.getStepExecution()
					.getExecutionContext()
					.putLong("processedCount", deletedCount);

				chunkContext.getStepContext()
					.getStepExecution()
					.getJobExecution()
					.getExecutionContext()
					.putLong("processedCount", deletedCount);

				return RepeatStatus.FINISHED;
			}, platformTransactionManager)
			.build();
	}

	@Bean
	public Job notificationDeleteJob() {
		return new JobBuilder(JobName.NOTIFICATION_DELETE_JOB.getName(), jobRepository)
			.start(notificationDeleteStep())
			.listener(jobProcessedCountListener)
			.build();
	}
}
