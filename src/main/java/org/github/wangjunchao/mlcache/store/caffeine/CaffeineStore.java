package org.github.wangjunchao.mlcache.store.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.github.wangjunchao.mlcache.store.AbstractStore;
import org.github.wangjunchao.mlcache.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 16:05
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CaffeineStore extends AbstractStore {

    private final static String DEFAULT_TYPE = "caffeine";
    private final static String DEFAULT_NAME = "caffeine-store";

    private Cache<String, Item> cache;
    /**
     * 实现命名锁，不同的名字使用不同的锁
     */
    private final Cache<String, Lock> lockCache = Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .expireAfterAccess(Duration.ofMinutes(5))
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();


    private Lock getLock(String name) {
        return lockCache.get(name, s -> new ReentrantLock());
    }

    {
        // 设置默认名称和类型
        setType(DEFAULT_TYPE);
        setName(DEFAULT_NAME);
    }

    @Override
    public boolean setnx(Object key, byte[] data) {
        String sk = this.getKeyCodec().getString(key);
        Lock lock = getLock(sk);

        lock.lock();
        try {
            Item item = cache.getIfPresent(sk);
            if (item == null) {
                item = Item.builder().data(data).build();
                cache.put(sk, item);
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public boolean setnx(Object key, byte[] data, Duration d) {
        String sk = this.getKeyCodec().getString(key);
        if (d == null) {
            d = DEFAULT_LIVE_TIME;
        }

        Lock lock = getLock(sk);
        lock.lock();
        try {
            Item item = cache.getIfPresent(sk);
            if (item == null) {
                item = Item.builder()
                        .data(data)
                        .expireAtNano(System.nanoTime() + d.toNanos())
                        .build();
                cache.put(sk, item);
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public Map<Object, Boolean> msetnx(Map<Object, byte[]> map) {

        if (CollectionUtils.isEmpty(map)) {
            return null;
        }

        final Map<Object, Boolean> result = Maps.newHashMap();
        map.forEach((key, data) -> {
            String sk = this.getKeyCodec().getString(key);
            Lock lock = getLock(sk);

            lock.lock();
            try {
                Item item = cache.getIfPresent(sk);
                if (item == null) {
                    item = Item.builder().data(data).build();
                    cache.put(sk, item);
                    result.put(key, true);
                } else {
                    result.put(key, false);
                }
            } finally {
                lock.unlock();
            }
        });

        return result;
    }

    @Override
    public Map<Object, Boolean> msetnx(Table<Object, byte[], Duration> table) {
        if (CollectionUtils.isEmpty(table)) {
            return null;
        }

        final Map<Object, Boolean> result = Maps.newHashMap();
        table.cellSet().forEach(cell -> {
            String sk = this.getKeyCodec().getString(cell.getRowKey());
            Lock lock = getLock(sk);

            lock.lock();
            try {
                Item item = cache.getIfPresent(sk);
                if (item == null) {
                    Duration d = cell.getValue() == null ? DEFAULT_LIVE_TIME : cell.getValue();
                    item = Item.builder()
                            .data(cell.getColumnKey())
                            .expireAtNano(System.nanoTime() + d.toNanos())
                            .build();
                    cache.put(sk, item);
                    result.put(cell.getRowKey(), true);
                } else {
                    result.put(cell.getRowKey(), false);
                }
            } finally {
                lock.unlock();
            }
        });

        return result;
    }

    @Override
    public boolean set(Object key, byte[] data) {
        String sk = this.getKeyCodec().getString(key);
        Lock lock = getLock(sk);

        lock.lock();
        try {
            Item item = Item.builder().data(data).build();
            cache.put(sk, item);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean set(Object key, byte[] data, Duration d) {
        String sk = this.getKeyCodec().getString(key);
        if (d == null) {
            d = DEFAULT_LIVE_TIME;
        }

        Lock lock = getLock(sk);
        lock.lock();
        try {
            Item item = Item.builder()
                    .data(data)
                    .expireAtNano(System.nanoTime() + d.toNanos())
                    .build();
            cache.put(sk, item);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Object, Boolean> mset(Map<Object, byte[]> map) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }

        final Map<Object, Boolean> result = Maps.newHashMap();
        map.forEach((k, v) -> {
            String sk = this.getKeyCodec().getString(k);
            Lock lock = getLock(sk);

            lock.lock();
            try {
                Item item = Item.builder().data(v).build();
                cache.put(sk, item);
                result.put(k, true);
            } finally {
                lock.unlock();
            }
        });

        return result;
    }

    @Override
    public Map<Object, Boolean> mset(Table<Object, byte[], Duration> table) {
        if (CollectionUtils.isEmpty(table)) {
            return null;
        }

        final Map<Object, Boolean> result = Maps.newHashMap();
        table.cellSet().forEach(cell -> {
            String sk = this.getKeyCodec().getString(cell.getRowKey());
            Lock lock = getLock(sk);

            lock.lock();
            try {
                Duration d = cell.getValue() == null ? DEFAULT_LIVE_TIME : cell.getValue();
                Item item = Item.builder()
                        .data(cell.getColumnKey())
                        .expireAtNano(System.nanoTime() + d.toNanos())
                        .build();
                cache.put(sk, item);
                result.put(cell.getRowKey(), true);

            } finally {
                lock.unlock();
            }
        });

        return result;
    }

    @Override
    public byte[] get(Object key) {
        Item item = cache.getIfPresent(this.getKeyCodec().getString(key));
        // 没有值，或者已经过期
        if (item == null || item.getData() == null || System.nanoTime() > item.getExpireAtNano()) {
            return null;
        }

        return item.getData();
    }

    @Override
    public List<byte[]> mget(Object... keys) {
        if (keys == null) {
            return null;
        }

        List<byte[]> result = Lists.newArrayListWithCapacity(keys.length);

        for (Object key : keys) {
            result.add(get(key));
        }

        return result;
    }

    @Override
    public void del(Object... keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        for (Object key : keys) {
            cache.invalidate(this.getKeyCodec().getString(key));
        }
    }
}
