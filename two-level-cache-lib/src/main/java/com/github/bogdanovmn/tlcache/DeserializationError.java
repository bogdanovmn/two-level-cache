package com.github.bogdanovmn.tlcache;

class DeserializationError extends TwoLvlCacheException {
	DeserializationError(Exception e) {
		super(e);
	}
}
