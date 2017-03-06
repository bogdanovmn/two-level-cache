package com.github.bogdanovmn.tlcache;

import java.util.List;

public class FileCache extends CacheWithMaxSizeLimit {
	public FileCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public List<ObjectInCache> releaseObjects(int size) {
		return null;
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
