package com.github.bogdanovmn.tlcache;

import java.util.*;

public class MemoryCache<KeyType> extends CacheWithMaxSizeLimit<KeyType, ObjectInCache> {

	public MemoryCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public List<ObjectInCache> releaseObjects(final int size) {
		int sizeToRelease = size - this.getFreeSpace();
		if (sizeToRelease < 0) {
			sizeToRelease = 0;
		}

		int releasedSize = 0;
		List<ObjectInCache> releasedObjects = new ArrayList<>();

		Set<KeyType> keysToRemove = new HashSet<>();
		for (Map.Entry<KeyType, ObjectInCache> entry : this.storage.entrySet()) {
			if (releasedSize < sizeToRelease) {
				ObjectInCache obj = entry.getValue();
				releasedSize += obj.size();
				releasedObjects.add(obj);
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
	public void put(KeyType key, ObjectInCache obj) {
		if (this.getFreeSpace() >= obj.size()) {
			this.storage.put(key, obj);
			this.currentSize += obj.size();
		}
	}

	@Override
	public ObjectInCache get(KeyType key) {
		return this.storage.get(key);
	}

	@Override
	public boolean delete(KeyType key) {
		ObjectInCache obj = this.storage.remove(key);
		if (obj != null) {
			this.currentSize -= obj.size();
			return true;
		}
		return false;
	}
}
