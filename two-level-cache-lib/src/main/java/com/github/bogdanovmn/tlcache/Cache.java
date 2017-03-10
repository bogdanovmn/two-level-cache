package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.SerializationError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;
import com.github.bogdanovmn.tlcache.exception.PutToCacheError;

interface Cache<KeyType, ObjType> {

	void put(final KeyType key, final ObjType value) throws SerializationError, PutToCacheError;

	ObjType get(final KeyType key) throws DeserializationError;

	boolean delete(final KeyType key);
}
