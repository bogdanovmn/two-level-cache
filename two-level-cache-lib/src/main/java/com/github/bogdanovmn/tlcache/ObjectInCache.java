package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.CreateCachedObjectError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;

import java.io.*;

public class ObjectInCache {
	private final String key;
	private final byte[] data;

	public ObjectInCache(String key, Object value) throws CreateCachedObjectError {
		try (
			ByteArrayOutputStream bos = new ByteArrayOutputStream()
		) {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(value);
			out.flush();
			this.data = bos.toByteArray();
			this.key = key;
		}
		catch (IOException e) {
			throw new CreateCachedObjectError(e);
		}
	}

	public Object fetch() throws DeserializationError {
		Object result;
		try (
			ByteArrayInputStream bis = new ByteArrayInputStream(this.data)
		) {
			ObjectInput in = new ObjectInputStream(bis);
			result = in.readObject();
		}
		catch (ClassNotFoundException | IOException e) {
			throw new DeserializationError(e);
		}
		return result;
	}


	public String getKey() {
		return this.key;
	}

	public int size() {
		return this.data.length;
	}
}
