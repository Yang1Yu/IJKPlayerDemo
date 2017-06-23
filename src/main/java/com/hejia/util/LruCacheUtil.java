package com.hejia.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 文件名：LruCacheUtil
 * 作者：韩秋宇
 * 时间：2017/5/28
 * 功能描述：图片缓存类
 */

public class LruCacheUtil {
    private static LruCache<String, Bitmap> lruCacheUtils; //LRU内存缓存

    public LruCacheUtil() {
    }

    public static LruCache getInstance() {
        if (lruCacheUtils == null) {
            long maxMemory = Runtime.getRuntime().maxMemory();//系统的全部缓存
            int cacheSize = (int) (maxMemory / 5);//设置系统全部缓存的1/5来缓存图片
            lruCacheUtils = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }
        return lruCacheUtils;
    }
}