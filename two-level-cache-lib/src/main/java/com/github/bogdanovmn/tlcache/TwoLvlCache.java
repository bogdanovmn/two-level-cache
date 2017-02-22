package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;

public class TwoLvlCache implements Cache {
	private final MemoryCache firstLvlCache;
	private final FileCache secondLvlCache;

	public TwoLvlCache(int memoryCacheMaxSize, int fileCacheMaxSize) {
		this.firstLvlCache = new MemoryCache(memoryCacheMaxSize);
		this.secondLvlCache = new FileCache(fileCacheMaxSize);
	}

	@Override
	public void put(String key, Object value)
		throws CreateCachedObjectError
	{
		CachedObject cachedObject = new CachedObject(value);
		this.delete(key);
		if (this.firstLvlCache.availableSize() >= cachedObject.size()) {
			this.firstLvlCache.put(key, cachedObject);
		}
		else {
			if (this.secondLvlCache.availableSize() >= cachedObject.size()) {
				this.secondLvlCache.put(key, cachedObject);
			}
			else {

			}
		}
	}

	@Override
	public Object get(String key)
		throws DeserializationError
	{
		Object result = null;

		CachedObject cachedObject = (CachedObject) this.firstLvlCache.get(key);
		if (cachedObject == null) {
			cachedObject = (CachedObject) this.secondLvlCache.get(key);
		}

		if (cachedObject != null) {
			result = cachedObject.fetch();
		}

		return result;
	}

	@Override
	public void delete(String key) {

	}
}
