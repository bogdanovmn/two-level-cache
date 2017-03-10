package com.github.bogdanovmn.tlcache.exception;

import java.io.IOException;

public class SerializationError extends Throwable {
	public SerializationError(IOException e) {
		super(e);
	}
}
