package com.github.bogdanovmn.tlcache.exception;

import java.io.IOException;

/**
 *
 */
public class PutToCacheError extends Throwable {
	public PutToCacheError(IOException e) {
		super(e);
	}
}
