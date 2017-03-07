package com.github.bogdanovmn.tlcache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MemoryCache extends CacheWithMaxSizeLimit {
	private final Map<String, ObjectInCache> storage = new LinkedHashMap<>();
	private int currentSize = 0;

	public MemoryCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public List<ObjectInCache> releaseObjects(int size) {
		int releasedSize = 0;
		List<ObjectInCache> releasedObjects = new ArrayList<>();

		for (Map.Entry<String, ObjectInCache> entry : this.storage.entrySet()) {
			if (releasedSize < size) {
				releasedSize += entry.getValue().size();
				releasedObjects.add(entry.getValue());
				this.delete(entry.getKey());
			}
			else {
				break;
			}
		}
		return releasedObjects;
	}

	@Override
	public void put(String key, Object value) {
		ObjectInCache obj = (ObjectInCache) value;
		if (this.getFreeSpace() >= obj.size()) {
			this.storage.put(key, obj);
			this.currentSize += obj.size();
		}
	}

	@Override
	public Object get(String key) {
		return this.storage.get(key);
	}

	@Override
	public boolean delete(String key) {
		ObjectInCache obj = this.storage.remove(key);
		if (obj != null) {
			this.currentSize -= obj.size();
			return true;
		}
		return false;
	}

	@Override
	public int getFreeSpace() {
		return this.maxSize - this.currentSize;
	}
}
