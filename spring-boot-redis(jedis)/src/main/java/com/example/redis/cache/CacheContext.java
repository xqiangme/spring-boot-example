package com.example.redis.cache;

public class CacheContext {
	public static ThreadLocal<Boolean> refreshFlag = new ThreadLocal<Boolean>();
	public static ThreadLocal<Boolean> onlyReadCache = new ThreadLocal<Boolean>();
	public static ThreadLocal<String> namespace = new ThreadLocal<String>();
	public static ThreadLocal<String> method = new ThreadLocal<String>();
}
