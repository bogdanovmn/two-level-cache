package com.github.bogdanovmn.tlcache;

import java.util.Map;

public class TwoLvlCacheStrategyAlpha implements TwoLvlCacheRotateStrategy {
	@Override
	public void rotateAndPut(
		AbstractCacheWithSizeLimit firstLvl,
		AbstractCacheWithSizeLimit secondLvl,
		Object key,
		byte[] data
	) throws PutToCacheError, SerializationError
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
