package com.github.bogdanovmn.tlcache;

public class FileCache extends CacheWithMaxSizeLimit {
	public FileCache(int maxSize) {
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
	public void delete(String key) {

	}
}
