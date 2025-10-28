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
	private final Job apiArticleCollectionJob;
	private final Job rssArticleCollectionJob;

	/**
	 * 이거 하드 코딩되어있음, 이거 조작하게 할 수 있지 않을까?
	 * 1. controller api 조정하게 할 수 있고
	 * 2. spring actuator도 되지 않을까
	 *  /acutator <- spring admin
	 * 	- spring admin 를 통해 로그 레벨을 조정할 수 있다.
	 * 	- 동적으로 yml
	 */
	@Scheduled(cron = "0 0/3 * * * *")
	public void runRssArticleCollectionJob() throws Exception {
		System.out.println("RSS 기사 배치 시작 스케줄러에 의해 실행");
		JobParameters params = new JobParametersBuilder()
			.addLong("rssArticleJob.id", System.currentTimeMillis())
			.toJobParameters();
		jobLauncher.run(rssArticleCollectionJob, params);
	}

	// @Scheduled(cron = "0 0/3 * * * *")
	public void runApiArticleCollectionJob() throws Exception {
		System.out.println("[API 네이버 배치 시작] 스케줄러에 의해 실행");
		apiArticleRunJob();
	}

	private void apiArticleRunJob() throws Exception {
		long nextPage = resolveStartPage();

		JobParameters params = new JobParametersBuilder()
			.addLong("apiArticleJob.id", System.currentTimeMillis())
			.addLong("apiArticleJob.page", nextPage)
			.toJobParameters();

		jobLauncher.run(apiArticleCollectionJob, params);
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
