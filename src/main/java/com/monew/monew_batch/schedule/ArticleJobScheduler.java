package com.monew.monew_batch.schedule;

import java.util.Comparator;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.monew.monew_batch.job.JobName;
import com.monew.monew_batch.job.common.monitor.BatchMetricsUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class ArticleJobScheduler {

	private final JobLauncher jobLauncher;
	private final JobExplorer jobExplorer;
	private final BatchMetricsUtil batchMetricsUtil;

	private final Job apiArticleCollectionJob;
	private final Job rssArticleCollectionJob;
	private final Job articleBackUpJob;
	private final Job notificationDeleteJob;

	// @Scheduled(cron = "0 0/2 * * * *")
	public void runNotificationDeleteJob() {
		JobName jobName = JobName.NOTIFICATION_DELETE_JOB;
		long startTime = System.currentTimeMillis();
		batchMetricsUtil.startBatch();

		try {
			log.info("[알림 삭제] 스케줄러에 의해 실행");
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("notificationDeleteJob", System.currentTimeMillis())
				.toJobParameters();

			JobExecution execution = jobLauncher.run(notificationDeleteJob, jobParameters);

			handleJobResult(jobName, execution, startTime);
		} catch (Exception e) {
			handleJobFailure(jobName, startTime, e);
		}
	}

	// @Scheduled(cron = "0 0/8 * * * *")
	public void runArticleToS3Job() {
		JobName jobName = JobName.ARTICLE_BACKUP_TO_S3_JOB;
		long startTime = System.currentTimeMillis();
		batchMetricsUtil.startBatch();

		try {
			log.info("[기사 S3 백업] 스케줄러에 의해 실행");
			JobParameters params = new JobParametersBuilder()
				.addLong("articleToS3Job.id", System.currentTimeMillis())
				.toJobParameters();

			JobExecution execution = jobLauncher.run(articleBackUpJob, params);

			handleJobResult(jobName, execution, startTime);
		} catch (Exception e) {
			handleJobFailure(jobName, startTime, e);
		}
	}

	// @Scheduled(cron = "0 0/3 * * * *")
	public void runRssArticleCollectionJob() {
		JobName jobName = JobName.RSS_ARTICLE_COLLECTION_JOB;
		long startTime = System.currentTimeMillis();
		batchMetricsUtil.startBatch();

		try {
			log.info("[RSS 기사 배치 시작] 스케줄러에 의해 실행");
			JobParameters params = new JobParametersBuilder()
				.addLong("rssArticleJob.id", System.currentTimeMillis())
				.toJobParameters();

			JobExecution execution = jobLauncher.run(rssArticleCollectionJob, params);

			handleJobResult(jobName, execution, startTime);
		} catch (Exception e) {
			handleJobFailure(jobName, startTime, e);
		}
	}

	@Scheduled(cron = "0 0/3 * * * *")
	public void runApiArticleCollectionJob() {
		JobName jobName = JobName.API_ARTICLE_COLLECTION_JOB;
		long startTime = System.currentTimeMillis();
		batchMetricsUtil.startBatch();
		long nextPage = resolveStartPage();

		try {
			log.info("[API 네이버 배치 시작] 스케줄러에 의해 실행");

			JobParameters params = new JobParametersBuilder()
				.addLong("apiArticleJob.id", System.currentTimeMillis())
				.addLong("apiArticleJob.page", nextPage)
				.toJobParameters();

			JobExecution execution = jobLauncher.run(apiArticleCollectionJob, params);

			handleJobResult(jobName, execution, startTime);
		} catch (Exception e) {
			handleJobFailure(jobName, startTime, e);
		}
	}

	private void handleJobResult(JobName jobName, JobExecution execution, long startTime) {

		long processedCount = execution.getExecutionContext().getLong("processedCount", 0L);

		long totalStepCount = execution.getStepExecutions().stream()
			.mapToLong(StepExecution::getWriteCount)
			.sum();
		long finalCount = Math.max(processedCount, totalStepCount);

		batchMetricsUtil.recordProcessedItems(jobName, (int)finalCount);
		log.info("[{}] 처리된 데이터 개수: {} 건", jobName.getName(), finalCount);

		if (execution.getStatus() == BatchStatus.COMPLETED) {
			batchMetricsUtil.recordSuccess(jobName);
			log.info("[{}] 배치 작업 성공", jobName.getName());
		} else {
			batchMetricsUtil.recordFailure(jobName);
			log.warn("[{}] 배치 작업 실패: {}", jobName.getName(), execution.getStatus());
		}

		long duration = System.currentTimeMillis() - startTime;
		batchMetricsUtil.recordExecutionTime(jobName, duration);

		batchMetricsUtil.recordLastExecutionTimeStamp(jobName);
		batchMetricsUtil.endBatch();
	}

	private void handleJobFailure(JobName jobName, long startTime, Exception e) {
		batchMetricsUtil.recordFailure(jobName);
		long duration = System.currentTimeMillis() - startTime;

		batchMetricsUtil.recordExecutionTime(jobName, duration);
		batchMetricsUtil.endBatch();

		log.error("[{}] 배치 작업 예외 발생", jobName.getName(), e);
	}

	private long resolveStartPage() {
		List<JobInstance> instances = jobExplorer.getJobInstances("articleCollectionJob", 0, 1);
		if (instances.isEmpty())
			return 1;

		List<JobExecution> execs = jobExplorer.getJobExecutions(instances.get(0));
		JobExecution latest = execs.stream()
			.sorted(Comparator.comparing(JobExecution::getCreateTime).reversed())
			.findFirst()
			.orElse(null);
		if (latest == null)
			return 1;

		Integer page = latest.getExecutionContext().getInt("currentPage", 1);
		return page.longValue();
	}
}