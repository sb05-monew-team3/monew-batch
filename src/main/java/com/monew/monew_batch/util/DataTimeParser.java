package com.monew.monew_batch.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DataTimeParser {
	public static Instant parseDateToInstant(String date) {
		return ZonedDateTime.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
	}

	public static String getDateFromInstant(Instant date) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd")
			.withZone(ZoneId.of("Asia/Seoul"))
			.format(date);
	}
}
