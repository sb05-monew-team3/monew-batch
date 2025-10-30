package com.monew.monew_batch.job;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobName {
	NOTIFICATION_DELETE_JOB("notificationDeleteJob"),
	ARTICLE_BACKUP_TO_S3_JOB("articleBackupToS3Job"),
	RSS_ARTICLE_COLLECTION_JOB("rssArticleCollectionJob"),
	API_ARTICLE_COLLECTION_JOB("apiArticleCollectionJob");

	private final String name;
}