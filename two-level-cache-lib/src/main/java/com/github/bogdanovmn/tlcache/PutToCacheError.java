package com.github.bogdanovmn.tlcache;

import java.io.IOException;

class PutToCacheError extends TwoLvlCacheException {
	PutToCacheError(IOException e) {
		super(e);
	}
}
