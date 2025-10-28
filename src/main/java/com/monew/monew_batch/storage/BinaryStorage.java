package com.monew.monew_batch.storage;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public interface BinaryStorage {
	UUID put(UUID id, Instant date, byte[] data);

	InputStream get(UUID id, Instant date);
}