package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;

public interface Cache<KeyType, ObjType> {

	void put(final KeyType key, final ObjType value) throws CreateCachedObjectError;

	ObjType get(final KeyType key) throws DeserializationError;

	boolean delete(final KeyType key);
}
