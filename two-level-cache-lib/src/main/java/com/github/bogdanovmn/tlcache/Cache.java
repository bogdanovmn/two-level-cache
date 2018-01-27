package com.github.bogdanovmn.tlcache;

interface Cache<KeyType, ObjType> {

	void put(final KeyType key, final ObjType value) throws PutToCacheError, SerializationError;

	ObjType get(final KeyType key) throws DeserializationError, RotateObjectError;

	boolean delete(final KeyType key);
}
