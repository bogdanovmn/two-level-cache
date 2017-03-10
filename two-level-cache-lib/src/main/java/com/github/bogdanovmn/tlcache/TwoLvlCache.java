package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.PutToCacheError;
import com.github.bogdanovmn.tlcache.exception.SerializationError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;
import com.github.bogdanovmn.tlcache.strategy.CacheRotateStrategy;

import java.io.*;

public class TwoLvlCache<KeyType, ObjType> implements Cache<KeyType, ObjType> {
	private final AbstractCacheWithSizeLimit firstLvlCache;
	private final AbstractCacheWithSizeLimit secondLvlCache;
	private final CacheRotateStrategy rotateStrategy;

	public TwoLvlCache(int memoryCacheMaxSize, int fileCacheMaxSize, CacheRotateStrategy strategy)
		throws IOException
	{
		this.firstLvlCache = new MemoryCache<KeyType>(memoryCacheMaxSize);
//		this.secondLvlCache = new MemoryCache<KeyType>(fileCacheMaxSize);
		this.secondLvlCache = new FileCache<KeyType>(fileCacheMaxSize, System.getProperty("java.io.tmpdir"));
		this.rotateStrategy = strategy;
	}

	@Override
	public void put(KeyType key, ObjType obj)
		throws SerializationError, PutToCacheError
	{
		byte[] data = this.toBytes(obj);

		this.delete(key);
		this.rotateStrategy.rotateAndPut(this.firstLvlCache, this.secondLvlCache, key, data);
	}

	@Override
	public ObjType get(KeyType key)
		throws DeserializationError
	{
		ObjType result = null;

		byte[] data = (byte[]) this.firstLvlCache.get(key);
		if (data == null) {
			data = (byte[]) this.secondLvlCache.get(key);
		}

		if (data != null) {
			result = this.fromBytes(data);
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

	private byte[] toBytes(ObjType obj)
		throws SerializationError
	{
		byte[] result;

		try (
			ByteArrayOutputStream bos = new ByteArrayOutputStream()
		) {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			out.flush();
			result = bos.toByteArray();
		}
		catch (IOException e) {
			throw new SerializationError(e);
		}

		return result;
	}

	public ObjType fromBytes(final byte[] data)
		throws DeserializationError
	{
		ObjType result;
		try (
			ByteArrayInputStream bis = new ByteArrayInputStream(data)
		) {
			ObjectInput in = new ObjectInputStream(bis);
			result = (ObjType) in.readObject();
		}
		catch (ClassNotFoundException | IOException e) {
			throw new DeserializationError(e);
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format(
			"[ %d / %d ] :: (%s) ::: (%s)",
				this.firstLvlCache.getCurrentSize(),
				this.secondLvlCache.getCurrentSize(),
				this.firstLvlCache.getKeys(),
				this.secondLvlCache.getKeys()
			);
	}
}
