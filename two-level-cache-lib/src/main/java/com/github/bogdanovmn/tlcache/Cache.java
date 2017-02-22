package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;

public interface Cache {

	void put(final String key, final Object value) throws CreateCachedObjectError;

	Object get(final String key) throws DeserializationError;

	void delete(final String key);

}
