package com.github.bogdanovmn.tlcache.strategy;

import com.github.bogdanovmn.tlcache.Cache;
import com.github.bogdanovmn.tlcache.CacheLevel;
import com.github.bogdanovmn.tlcache.ObjectInCache;
import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;

import java.util.List;

public class TwoLevelCacheStrategyAlpha implements CacheRotateStrategy {
	@Override
	public void rotateAndPut(Cache firstLvl, Cache secondLvl, ObjectInCache obj)
		throws CreateCachedObjectError
	{
		if (firstLvl.getFreeSpace() >= obj.size()) {
			firstLvl.put(obj.getKey(), obj);
		}
		else {
			List<ObjectInCache> objects = firstLvl.releaseObjects(obj.size());
			secondLvl.releaseObjects(obj.size());
			secondLvl.put(objects);
			firstLvl.put(obj.getKey(), obj);
		}
	}
}
