package com.example.util;

public class CacheContext {

    /**
     * 不读取缓存设置（优先级最高）
     */
    public static ThreadLocal<Boolean> notReadCacheFlag = new ThreadLocal<Boolean>();

    /**
     * 刷新缓存设置
     * 注：如果已经设置了 notReadCacheFlag=true,则刷新缓存不生效
     */
    public static ThreadLocal<Boolean> refreshCacheFlag = new ThreadLocal<Boolean>();

}
