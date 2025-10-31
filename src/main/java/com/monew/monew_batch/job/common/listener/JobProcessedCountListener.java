package com.monew.monew_batch.job.common.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobProcessedCountListener implements JobExecutionListener {

	@Override
	public void afterJob(JobExecution jobExecution) {
		long totalProcessedCount = jobExecution.getStepExecutions().stream()
			.mapToLong(stepExecution ->
				stepExecution.getExecutionContext().getLong("processedCount", 0L))
			.sum();

		jobExecution.getExecutionContext().putLong("processedCount", totalProcessedCount);

		log.info("[{}] 전체 처리 개수: {} 건",
			jobExecution.getJobInstance().getJobName(),
			totalProcessedCount);
	}
}
