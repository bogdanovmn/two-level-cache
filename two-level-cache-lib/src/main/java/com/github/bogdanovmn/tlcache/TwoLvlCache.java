package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;
import com.github.bogdanovmn.tlcache.strategy.CacheRotateStrategy;

public class TwoLvlCache<KeyType, ObjType> implements Cache<KeyType, ObjType> {
	private final CacheWithMaxSizeLimit firstLvlCache;
	private final CacheWithMaxSizeLimit secondLvlCache;
	private final CacheRotateStrategy rotateStrategy;

	public TwoLvlCache(int memoryCacheMaxSize, int fileCacheMaxSize, CacheRotateStrategy strategy) {
		this.firstLvlCache = new MemoryCache<KeyType>(memoryCacheMaxSize);
		this.secondLvlCache = new MemoryCache<KeyType>(fileCacheMaxSize);
//		this.secondLvlCache = new FileCache<KeyType>(fileCacheMaxSize);
		this.rotateStrategy = strategy;
	}

	@Override
	public void put(KeyType key, ObjType obj)
		throws CreateCachedObjectError
	{
		ObjectInCache objectInCache = new ObjectInCache(key, obj);

		this.delete(key);
		this.rotateStrategy.rotateAndPut(this.firstLvlCache, this.secondLvlCache, objectInCache);
	}

	@Override
	public ObjType get(KeyType key)
		throws DeserializationError
	{
		ObjType result = null;

		ObjectInCache<KeyType, ObjType> objectInCache = (ObjectInCache<KeyType, ObjType>) this.firstLvlCache.get(key);
		if (objectInCache == null) {
			objectInCache = (ObjectInCache<KeyType, ObjType>) this.secondLvlCache.get(key);
		}

		if (objectInCache != null) {
			result = objectInCache.fetch();
		}

		return result;
	}

	@Override
	public boolean delete(KeyType key) {
		boolean result = this.firstLvlCache.delete(key);
		if (!result) {
			result = this.secondLvlCache.delete(key);
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format(
			"[ %d / %d ] ::: (%s) ::: (%s)",
				this.firstLvlCache.getCurrentSize(),
				this.secondLvlCache.getCurrentSize(),
				this.firstLvlCache.getKeys(),
				this.secondLvlCache.getKeys()
			);
	}
}
