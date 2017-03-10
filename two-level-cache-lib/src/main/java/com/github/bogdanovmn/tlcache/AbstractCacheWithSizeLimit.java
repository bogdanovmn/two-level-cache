package com.github.bogdanovmn.tlcache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractCacheWithSizeLimit<KeyType> implements Cache<KeyType, byte[]> {
	protected final Map<KeyType, Object> storage = new LinkedHashMap<>();
	private final int maxSize;
	protected int currentSize = 0;


	public AbstractCacheWithSizeLimit(int maxSize) {
		this.maxSize = maxSize;
	}


	abstract public Map<KeyType, byte[]> releaseObjects(int size);

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
