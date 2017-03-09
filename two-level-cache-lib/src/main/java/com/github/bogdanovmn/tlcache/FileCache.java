package com.github.bogdanovmn.tlcache;

import java.util.List;

public class FileCache<KeyType> extends CacheWithMaxSizeLimit<KeyType, ObjectInCache> {
	public FileCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public List<ObjectInCache> releaseObjects(int size) {
		return null;
	}

	@Override
	public void put(KeyType key, ObjectInCache value) {

	}

	@Override
	public ObjectInCache get(KeyType key) {
		return null;
	}

	@Override
	public boolean delete(KeyType key) {
		return false;
	}
}
