package com.github.bogdanovmn.tlcache;

import java.util.Map;

public class TwoLvlCacheStrategyBeta implements TwoLvlCacheRotateStrategy {
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
			Map<Object, byte[]> objects = firstLvl.releaseObjects(data.length * 2);
			secondLvl.releaseObjects(data.length);
			for (Map.Entry o : objects.entrySet()) {
				secondLvl.put(o.getKey(), o.getValue());
			}
			firstLvl.put(key, data);
		}
	}
}
