package com.github.bogdanovmn.tlcache.strategy;

import com.github.bogdanovmn.tlcache.AbstractCacheWithSizeLimit;
import com.github.bogdanovmn.tlcache.exception.SerializationError;
import com.github.bogdanovmn.tlcache.exception.PutToCacheError;

public interface CacheRotateStrategy {
	void rotateAndPut(
		AbstractCacheWithSizeLimit firstLvl,
		AbstractCacheWithSizeLimit secondLvl,
		Object key,
		byte[] data
	) throws SerializationError, PutToCacheError;
}
