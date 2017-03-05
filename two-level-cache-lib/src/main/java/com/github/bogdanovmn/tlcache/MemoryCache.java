package com.github.bogdanovmn.tlcache;

public class MemoryCache extends CacheWithMaxSizeLimit {
	public MemoryCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public void put(String key, Object value) {

	}

	@Override
	public Object get(String key) {
		return null;
	}

	@Override
	public boolean delete(String key) {
		return false;
	}

	@Override
	public int getFreeSpace() {
		return 0;
	}
}
