package com.github.bogdanovmn.tlcache;

import java.util.List;

abstract public class CacheWithMaxSizeLimit implements Cache {
	protected final int maxSize;

	public CacheWithMaxSizeLimit(int maxSize) {
		this.maxSize = maxSize;
	}

	abstract public List<ObjectInCache> releaseObjects(int size);

	abstract public int getFreeSpace();

}
