package com.github.bogdanovmn.tlcache.strategy;

import com.github.bogdanovmn.tlcache.AbstractCacheWithSizeLimit;
import com.github.bogdanovmn.tlcache.exception.SerializationError;
import com.github.bogdanovmn.tlcache.exception.PutToCacheError;

import java.util.List;
import java.util.Map;

public class TwoLevelCacheStrategyAlpha implements CacheRotateStrategy {
	@Override
	public void rotateAndPut(
		AbstractCacheWithSizeLimit firstLvl,
		AbstractCacheWithSizeLimit secondLvl,
		Object key,
		byte[] data
	) throws SerializationError, PutToCacheError
	{
		if (firstLvl.getFreeSpace() >= data.length) {
			firstLvl.put(key, data);
		}
		else {
			Map<Object, byte[]> objects = firstLvl.releaseObjects(data.length);
			secondLvl.releaseObjects(data.length);
			for (Map.Entry o : objects.entrySet()) {
				secondLvl.put(o.getKey(), o.getValue());
			}
			firstLvl.put(key, data);
		}
	}
}
