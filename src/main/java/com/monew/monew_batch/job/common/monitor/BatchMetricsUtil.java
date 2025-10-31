package com.monew.monew_batch.job.common.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.monew.monew_batch.job.JobName;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchMetricsUtil {
	private final MeterRegistry meterRegistry;

	private final AtomicInteger runningBatchCount = new AtomicInteger(0);

	private final Map<String, Counter> successCounters = new ConcurrentHashMap<>();
	private final Map<String, Counter> failCounters = new ConcurrentHashMap<>();

	private final Map<String, Timer> totalExecutionTimers = new ConcurrentHashMap<>();
	private final Map<String, AtomicLong> lastExecutionDuration = new ConcurrentHashMap<>();
	private final Map<String, AtomicLong> lastExecutionTimestamp = new ConcurrentHashMap<>();

	private final Map<String, AtomicLong> totalProcessedCounts = new ConcurrentHashMap<>();

	private final Map<String, AtomicLong> lastBatchProcessedCounts = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		try {
			Gauge.builder("batch.running.count", runningBatchCount, AtomicInteger::get)
				.description("현재 실행 중인 배치 작업 수")
				.register(meterRegistry);

			initJobMetrics(JobName.API_ARTICLE_COLLECTION_JOB);
			initJobMetrics(JobName.RSS_ARTICLE_COLLECTION_JOB);
			initJobMetrics(JobName.ARTICLE_BACKUP_TO_S3_JOB);
			initJobMetrics(JobName.NOTIFICATION_DELETE_JOB);

			log.info("배치 메트릭 초기화 완료");
		} catch (Exception e) {
			log.error("배치 메트릭 초기화 실패", e);
		}
	}

	private void initJobMetrics(JobName jobName) {
		String name = jobName.getName();

		successCounters.put(name,
			Counter.builder("batch.job.success")
				.description("배치 작업 성공 횟수")
				.tag("job", name)
				.register(meterRegistry)
		);

		AtomicLong lastDuration = new AtomicLong(0);
		lastExecutionDuration.put(name, lastDuration);
		Gauge.builder("batch.last.execution.time", lastDuration, AtomicLong::get)
			.description("마지막 배치 실행 소요 시간 (ms)")
			.tag("job", name)
			.register(meterRegistry);

		failCounters.put(name,
			Counter.builder("batch.job.fail")
				.description("배치 작업 실패 횟수")
				.tag("job", name)
				.register(meterRegistry)
		);

		totalExecutionTimers.put(name,
			Timer.builder("batch.total.execution.time")
				.description("배치 작업 실행 시간")
				.tag("job", name)
				.register(meterRegistry)
		);

		AtomicLong lastTime = new AtomicLong(0);
		lastExecutionTimestamp.put(name, lastTime);
		Gauge.builder("batch.last.execution.timestamp", lastTime, AtomicLong::get)
			.description("마지막 배치 실행 타임스탬프(ms)")
			.tag("job", name)
			.register(meterRegistry);

		AtomicLong totalCount = new AtomicLong(0);
		totalProcessedCounts.put(name, totalCount);
		Gauge.builder("batch.total.processed.count", totalCount, AtomicLong::get)
			.description("전체 누적 처리 데이터 개수")
			.tag("job", name)
			.register(meterRegistry);

		AtomicLong lastBatchCount = new AtomicLong(0);
		lastBatchProcessedCounts.put(name, lastBatchCount);
		Gauge.builder("batch.last.processed.count", lastBatchCount, AtomicLong::get)
			.description("마지막 배치에서 처리한 데이터 개수")
			.tag("job", name)
			.register(meterRegistry);

		log.debug("Job 메트릭 초기화 완료: {}", name);
	}

	public void startBatch() {
		runningBatchCount.incrementAndGet();
		log.debug("배치 시작 - 실행 중인 배치 수: {}", runningBatchCount.get());
	}

	public void endBatch() {
		runningBatchCount.decrementAndGet();
		log.debug("배치 종료 - 실행 중인 배치 수: {}", runningBatchCount.get());
	}

	public void recordSuccess(JobName jobName) {
		Counter counter = successCounters.get(jobName.getName());

		counter.increment();
		log.debug("배치 성공 기록: {}", jobName);
	}

	public void recordFailure(JobName jobName) {
		Counter counter = failCounters.get(jobName.getName());

		counter.increment();
		log.warn("배치 실패 기록: {}", jobName);
	}

	public void recordProcessedItems(JobName jobName, int count) {
		log.debug("처리된 아이템 기록: {} - {} 건", jobName.getName(), count);

		AtomicLong totalCount = totalProcessedCounts.get(jobName.getName());
		if (totalCount != null) {
			totalCount.addAndGet(count);
		}

		AtomicLong lastBatchCount = lastBatchProcessedCounts.get(jobName.getName());
		if (lastBatchCount != null) {
			lastBatchCount.set(count);
		}
	}

	public void recordExecutionTime(JobName jobName, long milliseconds) {
		Timer timer = totalExecutionTimers.get(jobName.getName());
		long duration = Math.max(1, milliseconds);

		timer.record(duration, TimeUnit.MILLISECONDS);

		AtomicLong lastTime = lastExecutionDuration.get(jobName.getName());
		lastTime.set(duration);

		log.debug("실행 시간 기록: {} - {}ms", jobName, milliseconds);
	}

	public void recordLastExecutionTimeStamp(JobName jobName) {
		AtomicLong lastTime = lastExecutionTimestamp.get(jobName.getName());

		long timestamp = System.currentTimeMillis();
		lastTime.set(timestamp);

	}

}