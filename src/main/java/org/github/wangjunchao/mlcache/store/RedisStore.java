package org.github.wangjunchao.mlcache.store;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import io.lettuce.core.KeyValue;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.github.wangjunchao.mlcache.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:56
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RedisStore extends AbstractStore {
    private final static String DEFAULT_TYPE = "redis";
    private final static String DEFAULT_NAME = "redis-store";
    private final static String OK = "OK";

    /**
     * Redis 链接信息, TODO 优化成连接池
     */
    private StatefulRedisConnection<String, byte[]> connect;

    {
        // 设置默认名称和类型
        setType(DEFAULT_TYPE);
        setName(DEFAULT_NAME);
    }


    @Override
    public boolean setnx(Object key, byte[] data) {
        return getSyncCommands().setnx(getKeyCreator().getString(key), data);
    }

    @Override
    public boolean setnx(Object key, byte[] data, Duration d) {
        Table<Object, byte[], Duration> table = HashBasedTable.create();
        table.put(key, data, d);
        return msetnx(table);
    }


    /**
     * 全部成功返回ture，部分成功不返回结果
     *
     * @param map
     * @return
     */
    @Override
    public Map<Object, Boolean> msetnx(Map<Object, byte[]> map) {

        if (CollectionUtils.isEmpty(map)) {
            return Maps.newHashMap();
        }
        final Map<String, byte[]> m2 = getMap(map);

        boolean success = getSyncCommands().msetnx(m2);
        return getObjectBooleanMap(map, success);
    }

    private Map<Object, Boolean> getObjectBooleanMap(Map<Object, byte[]> map, boolean success) {
        Map<Object, Boolean> result = Maps.newHashMap();
        if (success) {
            map.keySet().forEach(k -> result.put(k, true));
        }

        return result;
    }

    /**
     *
     * @param table
     * @return
     */
    @Override
    public Map<Object, Boolean> msetnx(Table<Object, byte[], Duration> table) {

        if (CollectionUtils.isEmpty(table)) {
            return Maps.newHashMap();
        }

        RedisAsyncCommands<String, byte[]> asyncCommands = getAsyncCommands();
        asyncCommands.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = Lists.newArrayList();
        table.cellSet().forEach(c -> {
            String sk = getKeyCreator().getString(c.getRowKey());
            byte[] data = c.getColumnKey();
            Duration d = c.getValue();
            futures.add(asyncCommands.setnx(sk, data));
            futures.add(asyncCommands.expire(sk, d));
        });

        asyncCommands.flushCommands();

        boolean success = LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
                futures.toArray(new RedisFuture[futures.size()]));

        Map<Object, Boolean> result = Maps.newHashMap();
        if (success) {
            table.cellSet().forEach(c -> result.put(c.getRowKey(), true));
        }

        return result;
    }


    @Override
    public boolean set(Object key, byte[] data) {
        String ok = getSyncCommands().set(getKeyCreator().getString(key), data);
        return OK.equalsIgnoreCase(ok);
    }

    @Override
    public boolean set(Object key, byte[] data, Duration d) {
        String sk = getKeyCreator().getString(key);
        SetArgs ex = SetArgs.Builder.ex(d);
        String ok = getSyncCommands().set(sk, data, ex);
        return OK.equalsIgnoreCase(ok);
    }

    @Override
    public boolean mset(Map<Object, byte[]> map) {
        if (CollectionUtils.isEmpty(map)) {
            return true;
        }
        final Map<String, byte[]> m2 = getMap(map);
        String ok = getSyncCommands().mset(m2);
        return OK.equalsIgnoreCase(ok);
    }

    @Override
    public boolean mset(Table<Object, byte[], Duration> table) {

        if (CollectionUtils.isEmpty(table)) {
            return true;
        }

        RedisAsyncCommands<String, byte[]> asyncCommands = getAsyncCommands();
        asyncCommands.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = Lists.newArrayList();
        table.cellSet().forEach(c -> {
            String sk = getKeyCreator().getString(c.getRowKey());
            byte[] data = c.getColumnKey();
            SetArgs ex = SetArgs.Builder.ex(c.getValue());
            futures.add(asyncCommands.set(sk, data, ex));
        });

        asyncCommands.flushCommands();
        return LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
                futures.toArray(new RedisFuture[futures.size()]));
    }

    @Override
    public <V> V get(Object key, Class<V> clazz) {
        byte[] bytes = getSyncCommands().get(getKeyCreator().getString(key));
        if (bytes == null) {
            return null;
        }

        return getValueCreator().getObject(clazz, bytes);
    }

    @Override
    public Map<Object, Object> mget(Map<Object, Class<?>> map) {

        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        final Map<String, Class<?>> m2 = new HashMap<>(map.size());
        // key 和他对应的 String
        final Map<String, Object> s2o = new HashMap<>(map.size());
        map.forEach((k, v) -> {
            String s = getKeyCreator().getString(k);
            m2.put(s, v);
            s2o.put(s, k);
        });

        List<KeyValue<String, byte[]>> rdsResult = getSyncCommands().mget(m2.keySet().toArray(new String[]{}));

        Map<Object, Object> result = new HashMap<>();
        if (CollectionUtils.isEmpty(rdsResult)) {
            return result;
        }

        for (KeyValue<String, byte[]> kv : rdsResult) {
            String k = kv.getKey();
            byte[] v = kv.getValue();
            if (v == null) {
                result.put(s2o.get(k), null);
            } else {
                Object o = getValueCreator().getObject(m2.get(k), v);
                result.put(s2o.get(k), o);
            }
        }

        return result;
    }

    @SafeVarargs
    @Override
    public final <K> void delete(K... keys) {

        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        String[] sk = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            sk[i] = getKeyCreator().getString(keys[i]);
        }

        getSyncCommands().del(sk);
    }

    //////////////////////////////////

    private Map<String, byte[]> getMap(Map<Object, byte[]> map) {
        final Map<String, byte[]> m2 = new HashMap<>();
        map.forEach((k, v) -> {
            m2.put(getKeyCreator().getString(k), v);
        });
        return m2;
    }

    /**
     * 获取同步连接
     *
     * @return
     */
    private RedisCommands<String, byte[]> getSyncCommands() {
        return connect.sync();
    }

    /**
     * 获取异步连接
     *
     * @return
     */
    private RedisAsyncCommands<String, byte[]> getAsyncCommands() {
        return connect.async();
    }
}
