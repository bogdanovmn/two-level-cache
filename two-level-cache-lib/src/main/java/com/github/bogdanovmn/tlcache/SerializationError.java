package com.github.bogdanovmn.tlcache;

import java.io.IOException;

class SerializationError extends TwoLvlCacheException {
	SerializationError(IOException e) {
		super(e);
	}
}
