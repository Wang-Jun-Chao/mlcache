package org.github.wangjunchao.mlcache.store.lettuce;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.internal.Exceptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.github.wangjunchao.mlcache.store.AbstractStore;
import org.github.wangjunchao.mlcache.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
public class LettuceStore extends AbstractStore {
    private final static String DEFAULT_TYPE = "redis-lettuce";
    private final static String DEFAULT_NAME = "redis-lettuce-store";
    private final static String OK = "OK";
    /**
     * 不存在设置值，并且设置生存时间
     */
    private final static String SETNXTTL = "-- 首先尝试使用setnx设置值\n" +
            "local result = redis.call('setnx', KEYS[1], ARGV[1]);\n" +
            "-- 如果成功，则设置key的失效时间\n" +
            "if result == 1 then\n" +
            "    local pexpire = redis.call('pexpire', KEYS[1], tonumber(ARGV[2]));\n" +
            "    if pexpire == 1 then\n" +
            "        -- 设置成功的话就返回空值，与后面返回的失效时间区别开\n" +
            "        return true;\n" +
            "    else\n" +
            "        -- 设置失败，删除原来的key，避免key长期有效\n" +
            "        redis.call('del', KEYS[1])\n" +
            "        return false\n" +
            "    end\n" +
            "else\n" +
            "    -- key操作失败，直接返回：0\n" +
            "    return false\n" +
            "end\n";

    /**
     * Redis 链接信息, TODO 优化成连接池
     */
    private StatefulRedisConnection<String, String> connect;

    private RedisCodec<String, String> codec;

    {
        // 设置默认名称和类型
        setType(DEFAULT_TYPE);
        setName(DEFAULT_NAME);
        // 设置默认编码
        setCodec(StringCodec.UTF8);
    }


    @Override
    public boolean setnx(Object key, byte[] data) {
        return getSyncCommands().setnx(
                this.getKeyCodec().getString(key),
                this.getValueCodec().getString(data));
    }

    @Override
    public boolean setnx(Object key, byte[] data, Duration d) {

        if (d == null) {
            d = DEFAULT_LIVE_TIME;
        }

        String[] keys = new String[]{this.getKeyCodec().getString(key)};
        String[] args = new String[]{this.getValueCodec().getString(data), Long.toString(d.toMillis())};

        return getSyncCommands().eval(SETNXTTL, ScriptOutputType.BOOLEAN, keys, args);
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
        final Map<String, String> m2 = getMap(map);

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
     * @param table
     * @return
     */
    @Override
    public Map<Object, Boolean> msetnx(Table<Object, byte[], Duration> table) {

        if (CollectionUtils.isEmpty(table)) {
            return Maps.newHashMap();
        }

        RedisAsyncCommands<String, String> asyncCommands = getAsyncCommands();
        asyncCommands.setAutoFlushCommands(false);
        List<RedisFuture<Boolean>> futures = Lists.newArrayList();
        List<Object> keyList = Lists.newArrayListWithCapacity(table.size());
        table.cellSet().forEach(c -> {
            Duration d = c.getValue() == null ? DEFAULT_LIVE_TIME : c.getValue();
            String[] keys = new String[]{this.getKeyCodec().getString(c.getRowKey())};
            String[] args = new String[]{this.getValueCodec().getString(c.getColumnKey()), Long.toString(d.toMillis())};

            keyList.add(c.getRowKey());
            RedisFuture<Boolean> future = asyncCommands.eval(SETNXTTL, ScriptOutputType.BOOLEAN, keys, args);
            futures.add(future);
        });

        asyncCommands.flushCommands();

        List<Boolean> futureResult = awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[0]));
        if (CollectionUtils.isEmpty(futureResult)) {
            return Maps.newHashMap();
        }

        Map<Object, Boolean> result = Maps.newHashMap();
        for (int i = 0; i < futureResult.size(); i += 2) {
            result.put(keyList.get(i), futureResult.get(i));
        }
        return result;
    }


    @Override
    public boolean set(Object key, byte[] data) {
        String ok = getSyncCommands().set(
                this.getKeyCodec().getString(key),
                this.getValueCodec().getString(data));
        return OK.equalsIgnoreCase(ok);
    }

    @Override
    public boolean set(Object key, byte[] data, Duration d) {
        String sk = this.getKeyCodec().getString(key);
        String sd = this.getValueCodec().getString(data);
        if (d == null) {
            d = DEFAULT_LIVE_TIME;
        }
        SetArgs ex = SetArgs.Builder.ex(d);
        String ok = getSyncCommands().set(sk, sd, ex);
        return OK.equalsIgnoreCase(ok);
    }

    @Override
    public Map<Object, Boolean> mset(Map<Object, byte[]> map) {
        Map<Object, Boolean> result = Maps.newHashMap();
        if (CollectionUtils.isEmpty(map)) {
            return result;
        }
        final Map<String, String> m2 = getMap(map);
        String ok = getSyncCommands().mset(m2);

        if (OK.equalsIgnoreCase(ok)) {
            map.forEach((k, v) -> result.put(k, true));
        }

        return result;
    }

    @Override
    public Map<Object, Boolean> mset(Table<Object, byte[], Duration> table) {
        Map<Object, Boolean> result = Maps.newHashMap();
        if (CollectionUtils.isEmpty(table)) {
            table.cellSet().forEach(c -> result.put(c.getRowKey(), true));
            return result;
        }

        RedisAsyncCommands<String, String> asyncCommands = getAsyncCommands();
        asyncCommands.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = Lists.newArrayList();
        table.cellSet().forEach(c -> {
            String sk = this.getKeyCodec().getString(c.getRowKey());
            String sd = this.getValueCodec().getString(c.getColumnKey());
            Duration d = c.getValue() == null ? DEFAULT_LIVE_TIME : c.getValue();
            SetArgs ex = SetArgs.Builder.ex(d);
            futures.add(asyncCommands.set(sk, sd, ex));
        });

        asyncCommands.flushCommands();
        boolean success = LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
                futures.toArray(new RedisFuture[0]));
        if (success) {
            table.cellSet().forEach(c -> result.put(c.getRowKey(), true));
        }

        return result;
    }

    @Override
    public byte[] get(Object key) {

        String value = getSyncCommands().get(this.getKeyCodec().getString(key));
        if (value == null) {
            return null;
        }

        return this.getValueCodec().getObject(byte[].class, value);
    }

    @Override
    public List<byte[]> mget(Object... keys) {
        if (keys == null) {
            return null;
        }

        String[] sks = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            sks[i] = this.getKeyCodec().getString(keys[i]);
        }

        List<KeyValue<String, String>> list = getSyncCommands().mget(sks);
        if (list == null) {
            return Lists.newArrayList();
        }
        List<byte[]> result = Lists.newArrayListWithCapacity(keys.length);
        list.forEach(item -> result.add(this.getValueCodec().getObject(byte[].class, item.getValue())));
        return result;
    }


    @Override
    public void del(Object... keys) {

        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        String[] sk = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            sk[i] = this.getKeyCodec().getString(keys[i]);
        }

        getSyncCommands().del(sk);
    }

    //////////////////////////////////

    private Map<String, String> getMap(Map<Object, byte[]> map) {
        final Map<String, String> result = new HashMap<>();
        map.forEach((k, v) -> {
            result.put(
                    this.getKeyCodec().getString(k),
                    this.getValueCodec().getString(v)
            );
        });
        return result;
    }

    /**
     * 获取同步连接
     *
     * @return
     */
    private RedisCommands<String, String> getSyncCommands() {
        return connect.sync();
    }

    /**
     * 获取异步连接
     *
     * @return
     */
    private RedisAsyncCommands<String, String> getAsyncCommands() {
        return connect.async();
    }

    /**
     * 等待所有结果
     *
     * @param timeout
     * @param unit
     * @param futures
     * @return
     */
    private static <T> List<T> awaitAll(final long timeout, TimeUnit unit, Future<T>... futures) {

        if (CollectionUtils.isEmpty(futures)) {
            return Lists.newArrayList();
        }

        List<T> result = Lists.newArrayListWithCapacity(futures.length);
        try {
            long nanos = unit.toNanos(timeout);
            long time = System.nanoTime();

            for (int i = 0; i < futures.length; i++) {
                Future<?> f = futures[i];
                // 小于等于0没有超时
                if (timeout <= 0) {
                    T t = (T) f.get();
                    result.add(i, t);
                } else {
                    if (nanos < 0) {
                        return result;
                    }
                    // TODO 有问题，如果第一个用时很长，会阻塞后面的结果返回
                    T o = (T) f.get(nanos, TimeUnit.NANOSECONDS);
                    result.add(i, o);

                    long now = System.nanoTime();
                    nanos -= now - time;
                    time = now;
                }
            }
            return result;
        } catch (TimeoutException e) {
            return result;
        } catch (Exception e) {
            throw Exceptions.fromSynchronization(e);
        }
    }


}
