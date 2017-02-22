package com.github.bogdanovmn.tlcache.exception;

import java.io.IOException;

public class CreateCachedObjectError extends Throwable {
	public CreateCachedObjectError(IOException e) {
		super(e);
	}
}
