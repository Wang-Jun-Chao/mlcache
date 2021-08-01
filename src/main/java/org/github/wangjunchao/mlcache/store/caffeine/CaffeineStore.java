//package org.github.wangjunchao.mlcache.store.caffeine;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.google.common.collect.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.SuperBuilder;
//import org.github.wangjunchao.mlcache.store.AbstractStore;
//import org.github.wangjunchao.mlcache.util.CollectionUtils;
//
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * <pre>
// *
// * </pre>
// * Author: 王俊超
// * Date: 2021-07-31 16:05
// * Blog: http://blog.csdn.net/derrantcm
// * Github: https://github.com/wang-jun-chao
// * All Rights Reserved !!!
// */
//@Data
//@SuperBuilder
//@NoArgsConstructor
//@AllArgsConstructor
//public class CaffeineStore extends AbstractStore {
//
//    @Data
//    @SuperBuilder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    private class Item {
//        /**
//         * 数据部分
//         */
//        private byte[] data;
//        /**
//         * 过期时间
//         */
//        private Long expireAt;
//    }
//
//
//    private final static String DEFAULT_TYPE = "caffeine";
//    private final static String DEFAULT_NAME = "caffeine-store";
//
//    private Cache<String, Item> cache;
//    private Lock lock = new ReentrantLock();
//
//    {
//        // 设置默认名称和类型
//        setType(DEFAULT_TYPE);
//        setName(DEFAULT_NAME);
//    }
//
//    @Override
//    public boolean setnx(Object key, byte[] data) {
//        lock.lock();
//        try {
//            String sk = this.getKeyCodec().getString(key);
//            Item item = cache.getIfPresent(sk);
//            if (item == null) {
//                item = Item.builder().data(data).build();
//                cache.put(sk, item);
//                return true;
//            }
//        } finally {
//            lock.unlock();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean setnx(Object key, byte[] data, Duration d) {
//        lock.lock();
//        try {
//            String sk = this.getKeyCodec().getString(key);
//            Item item = cache.getIfPresent(sk);
//            if (item == null) {
//                item = Item.builder().data(data).expireAt(System.currentTimeMillis() + d.toMillis()).build();
//                cache.put(sk, item);
//                return true;
//            }
//        } finally {
//            lock.unlock();
//        }
//        return false;
//    }
//
//    // TODO
//    @Override
//    public Map<Object, Boolean> msetnx(Map<Object, byte[]> map) {
//
//        if (CollectionUtils.isEmpty(map)) {
//            return null;
//        }
//
//        boolean success = true;
//        lock.lock();
//        try {
//            map.forEach((key, data)->{
//                String sk = this.getKeyCodec().getString(key);
//                Item item = cache.getIfPresent(sk);
//                if (item == null) {
//                    item = Item.builder().data(data).build();
//                    cache.put(sk, item);
//                }
//            });
//
//        } finally {
//            lock.unlock();
//        }
//        return null;
//    }
//
//    @Override
//    public Map<Object, Boolean> msetnx(Table<Object, byte[], Duration> table) {
//        return null;
//    }
//
//    @Override
//    public boolean set(Object key, byte[] data) {
//        return false;
//    }
//
//    @Override
//    public boolean set(Object key, byte[] data, Duration d) {
//        return false;
//    }
//
//    @Override
//    public Map<Object, Boolean> mset(Map<Object, byte[]> map) {
//        return null;
//    }
//
//    @Override
//    public Map<Object, Boolean> mset(Table<Object, byte[], Duration> table) {
//        return null;
//    }
//
//    @Override
//    public <V> V get(Object key, Class<V> clazz) {
//        return null;
//    }
//
//    @Override
//    public Map<Object, Object> mget(Map<Object, Class<?>> map) {
//        return null;
//    }
//
//    @Override
//    public <K> void delete(K... keys) {
//
//    }
//}
