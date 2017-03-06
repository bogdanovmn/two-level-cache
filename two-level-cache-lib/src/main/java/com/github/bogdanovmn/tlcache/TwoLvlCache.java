package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;
import com.github.bogdanovmn.tlcache.strategy.CacheRotateStrategy;

public class TwoLvlCache implements Cache {
	private final CacheWithMaxSizeLimit firstLvlCache;
	private final CacheWithMaxSizeLimit secondLvlCache;
	private final CacheRotateStrategy rotateStrategy;

	public TwoLvlCache(int memoryCacheMaxSize, int fileCacheMaxSize, CacheRotateStrategy strategy) {
		this.firstLvlCache = new MemoryCache(memoryCacheMaxSize);
		this.secondLvlCache = new FileCache(fileCacheMaxSize);
		this.rotateStrategy = strategy;
	}

	@Override
	public void put(String key, Object value)
		throws CreateCachedObjectError
	{
		ObjectInCache objectInCache = new ObjectInCache(key, value);

		this.delete(key);
		this.rotateStrategy.rotateAndPut(this.firstLvlCache, this.secondLvlCache, objectInCache);
	}

	@Override
	public Object get(String key)
		throws DeserializationError
	{
		Object result = null;

		ObjectInCache objectInCache = (ObjectInCache) this.firstLvlCache.get(key);
		if (objectInCache == null) {
			objectInCache = (ObjectInCache) this.secondLvlCache.get(key);
		}

		if (objectInCache != null) {
			result = objectInCache.fetch();
		}

		return result;
	}

	@Override
	public boolean delete(String key) {
		boolean result = this.firstLvlCache.delete(key);
		if (!result) {
			result = this.secondLvlCache.delete(key);
		}
		return result;
	}
}
