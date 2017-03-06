package com.github.bogdanovmn.tlcache.strategy;

import com.github.bogdanovmn.tlcache.CacheWithMaxSizeLimit;
import com.github.bogdanovmn.tlcache.ObjectInCache;
import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;

public interface CacheRotateStrategy {
	void rotateAndPut(CacheWithMaxSizeLimit firstLvl, CacheWithMaxSizeLimit secondLvl, ObjectInCache obj)
		throws CreateCachedObjectError;
}
