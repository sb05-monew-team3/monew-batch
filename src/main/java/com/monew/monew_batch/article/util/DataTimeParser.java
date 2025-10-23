package com.monew.monew_batch.article.util;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DataTimeParser {
	public static Instant parseDateToInstant(String date) {
		return ZonedDateTime.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
	}
}
