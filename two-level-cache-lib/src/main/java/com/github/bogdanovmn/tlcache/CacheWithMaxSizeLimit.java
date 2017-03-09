package com.github.bogdanovmn.tlcache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract public class CacheWithMaxSizeLimit<KeyType, ObjType> implements Cache<KeyType, ObjType> {
	protected final Map<KeyType, ObjType> storage = new LinkedHashMap<>();
	private final int maxSize;
	protected int currentSize = 0;


	public CacheWithMaxSizeLimit(int maxSize) {
		this.maxSize = maxSize;
	}

	abstract public List<ObjectInCache> releaseObjects(int size);

	public int getFreeSpace() {
		return this.maxSize - this.currentSize;
	}

	public int getCurrentSize() {
		return this.currentSize;
	}

	public Set<KeyType> getKeys() {
		return this.storage.keySet();
	}
}
