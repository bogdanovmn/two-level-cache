package com.github.bogdanovmn.tlcache;

public interface TwoLvlCacheRotateStrategy {
	void rotateAndPut(
		AbstractCacheWithSizeLimit firstLvl,
		AbstractCacheWithSizeLimit secondLvl,
		Object key,
		byte[] data
	) throws SerializationError, PutToCacheError;
}
