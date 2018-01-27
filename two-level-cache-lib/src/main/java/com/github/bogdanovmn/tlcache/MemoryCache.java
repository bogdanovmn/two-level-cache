package com.github.bogdanovmn.tlcache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class MemoryCache<KeyType> extends AbstractCacheWithSizeLimit<KeyType> {

	MemoryCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public Map<KeyType, byte[]> releaseObjects(final int size) {
		int sizeToRelease = size - this.getFreeSpace();
		if (sizeToRelease < 0) {
			sizeToRelease = 0;
		}

		int releasedSize = 0;
		Map<KeyType, byte[]> releasedObjects = new HashMap<>();

		Set<KeyType> keysToRemove = new HashSet<>();
		for (Map.Entry<KeyType, Object> entry : this.storage.entrySet()) {
			if (releasedSize < sizeToRelease) {
				byte[] data = (byte[]) entry.getValue();
				releasedSize += data.length;
				releasedObjects.put(entry.getKey(), data);
				keysToRemove.add(entry.getKey());
			}
			else {
				break;
			}
		}
		for (KeyType key : keysToRemove) {
			this.storage.remove(key);
		}

		this.currentSize -= releasedSize;

		return releasedObjects;
	}

	@Override
	public void put(KeyType key, final byte[] data) {
		if (this.getFreeSpace() >= data.length) {
			this.storage.put(key, data);
			this.currentSize += data.length;
		}
	}

	@Override
	public byte[] get(KeyType key) {
		return (byte[]) this.storage.get(key);
	}

	@Override
	public boolean delete(KeyType key) {
		byte[] data = (byte[]) this.storage.remove(key);
		if (data != null) {
			this.currentSize -= data.length;
			return true;
		}
		return false;
	}
}
