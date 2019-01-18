package com.sunkaisens.shard.config;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据缓存类
 */
public class GlobalHashMap {
	
	// 缓存所有的中间数据
	public static ConcurrentHashMap<String, Integer> cacheAlarmProcessDataMap = new ConcurrentHashMap<String, Integer>();

}
