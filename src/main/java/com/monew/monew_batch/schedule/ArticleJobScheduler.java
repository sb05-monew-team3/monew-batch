package com.monew.monew_batch.schedule;

import java.util.Comparator;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class ArticleJobScheduler {

	private final JobLauncher jobLauncher;
	private final JobExplorer jobExplorer;
	private final Job articleCollectionJob;

	// @EventListener(ApplicationReadyEvent.class)
	// public void runOnceAtStartup() throws Exception {
	// 	System.out.println("[배치 시작] 앱 시작 직후 1회 실행");
	// 	runJob();
	// }

	@Scheduled(cron = "0 0/1 * * * *")
	public void runArticleCollectionJob() throws Exception {
		System.out.println("[배치 시작] 스케줄러에 의해 실행");
		runJob();
	}

	private void runJob() throws Exception {
		long nextPage = resolveStartPage();

		JobParameters params = new JobParametersBuilder()
			.addLong("job.id", System.currentTimeMillis())
			.addLong("page", nextPage)
			.toJobParameters();

		jobLauncher.run(articleCollectionJob, params);
	}

	private long resolveStartPage() {
		List<JobInstance> instances = jobExplorer.getJobInstances("articleCollectionJob", 0, 1);
		if (instances.isEmpty())
			return 1L;

		// 해당 인스턴스의 가장 최근 JobExecution 선택 (완료 우선)
		List<JobExecution> execs = jobExplorer.getJobExecutions(instances.get(0));
		JobExecution latest = execs.stream()
			.sorted(Comparator.comparing(JobExecution::getCreateTime).reversed())
			.findFirst()
			.orElse(null);
		if (latest == null)
			return 1L;

		// JobExecutionContext에서 currentPage 꺼냄 (없으면 1)
		Integer page = latest.getExecutionContext().getInt("currentPage", 1);
		return page.longValue();
	}
}
