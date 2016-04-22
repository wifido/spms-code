package com.sf.module.common.cache;

import java.util.Map;
import com.sf.framework.server.core.cache.CacheManager;
import com.sf.framework.server.core.cache.ICache;

public class SpmsSysConfigCache {

	private static ICache sysConfigCache = CacheManager.getInstance().getCache("cache_spms_sysconfig_modules");

	@SuppressWarnings("unchecked")
	public static Map<String, String> getCache() {
		return (Map<String, String>) sysConfigCache.getData(null);
	}

	public static String getCacheByKeyName(String keyName) {
		Map<String, String> cache = (Map<String, String>) sysConfigCache.getData(null);
		return cache.get(keyName);
	}
}
