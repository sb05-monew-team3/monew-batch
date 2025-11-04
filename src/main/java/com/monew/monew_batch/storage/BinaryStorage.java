package com.monew.monew_batch.storage;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public interface BinaryStorage {
	UUID put(UUID id, Instant date, String interest, byte[] data);

	InputStream get(UUID id, String interest, Instant date);

	Boolean exists(UUID id, String interest, Instant date);
}