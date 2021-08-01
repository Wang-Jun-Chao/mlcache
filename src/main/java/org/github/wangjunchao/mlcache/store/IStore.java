package org.github.wangjunchao.mlcache.store;

import com.google.common.collect.Table;
import org.github.wangjunchao.mlcache.codec.ICodec;
import org.github.wangjunchao.mlcache.metrics.IMetrics;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:44
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public interface IStore {
    /**
     * 缓存名称
     *
     * @return
     */
    String getName();

    /**
     * 缓存的具体实现类型，每种缓存都要唯一
     *
     * @return
     */
    String getType();

    boolean setnx(Object key, byte[] data);
    boolean setnx(Object key, byte[] data, Duration d);

    Map<Object, Boolean> msetnx(Map<Object, byte[]> map);
    Map<Object, Boolean> msetnx(Table<Object, byte[], Duration> table);

    boolean set(Object key, byte[] data);
    boolean set(Object key, byte[] data, Duration d);

    Map<Object, Boolean> mset(Map<Object, byte[]> map);
    Map<Object, Boolean> mset(Table<Object, byte[], Duration> table);

    byte[] get(Object key);
    List<byte[]> mget(Object ...keys);

    Map<Object, Object> mget(Map<Object, Class<?>> map);

    <K> void del(K ...keys);

    ICodec getKeyCodec();

    IMetrics getMetrics();

    ICodec getValueCodec();
}
