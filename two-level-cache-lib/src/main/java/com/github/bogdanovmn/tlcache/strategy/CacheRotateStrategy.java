package com.github.bogdanovmn.tlcache.strategy;

import com.github.bogdanovmn.tlcache.Cache;
import com.github.bogdanovmn.tlcache.ObjectInCache;
import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;

public interface CacheRotateStrategy {
	void rotateAndPut(Cache firstLvl, Cache secondLvl, ObjectInCache obj) throws CreateCachedObjectError;
}
