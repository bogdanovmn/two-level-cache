package com.github.bogdanovmn.tlcache;

import com.github.bogdanovmn.tlcache.exception.PutToCacheError;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class FileCache<KeyType> extends AbstractCacheWithSizeLimit<KeyType> {
	private final Map<KeyType, File> files = new HashMap<>();
	private final Path baseDir;

	FileCache(int maxSize, String cacheDir)
		throws IOException
	{
		super(maxSize);

		this.baseDir = Files.createTempDirectory(
			Paths.get(
				cacheDir
			), ""
		);
	}

	@Override
	public Map<KeyType, byte[]> releaseObjects(int size) {
		int sizeToRelease = size - this.getFreeSpace();
		if (sizeToRelease < 0) {
			sizeToRelease = 0;
		}

		int releasedSize = 0;

		Set<KeyType> keysToRemove = new HashSet<>();
		for (Map.Entry<KeyType, Object> entry : this.storage.entrySet()) {
			if (releasedSize < sizeToRelease) {
				releasedSize += (int) entry.getValue();
				keysToRemove.add(entry.getKey());
			}
			else {
				break;
			}
		}
		for (KeyType key : keysToRemove) {
			this.storage.remove(key);
			File fileToRemove = this.files.get(key);
			fileToRemove.delete();
			this.files.remove(key);
		}

		this.currentSize -= releasedSize;

		return null;
	}

	@Override
	public void put(KeyType key, byte[] data)
		throws PutToCacheError
	{
		if (this.getFreeSpace() >= data.length) {
			File outputFile;
			try {
				outputFile = Files.createTempFile(this.baseDir, "", "").toFile();
			}
			catch (IOException e) {
				throw new PutToCacheError(e);
			}

			try (
				FileOutputStream output = new FileOutputStream(outputFile);
			) {
				output.write(data);
				output.flush();
			}
			catch (IOException e) {
				throw new PutToCacheError(e);
			}

			this.files.put(key, outputFile);
			this.storage.put(key, data.length);
			this.currentSize += data.length;
		}
	}

	@Override
	public byte[] get(KeyType key) {
		byte[] result = null;

		File file = this.files.get(key);
		if (file != null) {
			try {
				result = Files.readAllBytes(file.toPath());
			}
			catch (IOException e) {
				return null;
			}
		}
		return result;
	}

	@Override
	public boolean delete(KeyType key) {
		File file = this.files.remove(key);
		if (file != null) {
			try {
				Files.delete(file.toPath());
				this.currentSize -= (int) this.storage.remove(key);
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		return false;
	}
}
