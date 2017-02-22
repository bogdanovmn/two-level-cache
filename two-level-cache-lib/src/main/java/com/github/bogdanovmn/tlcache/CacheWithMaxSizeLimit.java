package com.github.bogdanovmn.tlcache;

abstract class CacheWithMaxSizeLimit implements Cache {
	private final int maxSize;

	public CacheWithMaxSizeLimit(int maxSize) {
		this.maxSize = maxSize;
	}
}
