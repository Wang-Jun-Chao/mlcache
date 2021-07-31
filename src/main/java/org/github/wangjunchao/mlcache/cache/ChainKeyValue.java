package org.github.wangjunchao.mlcache.cache;

import java.time.Duration;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 10:43
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class ChainKeyValue<K> {
    /**
     * 缓存原始 key
     */
    private K key;
    /**
     * 缓存数据
     */
    private byte[] value;
    /**
     * 缓存的过期时间
     */
    private Duration ttl;
    /**
     * 哪个缓存服务产生的缓存
     */
    private String storeType;
    /**
     * 产生的缓存属于哪个级别
     */
    private int level;
}
