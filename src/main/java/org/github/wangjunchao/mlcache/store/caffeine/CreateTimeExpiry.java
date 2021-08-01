package org.github.wangjunchao.mlcache.store.caffeine;

import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 14:15
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class CreateTimeExpiry implements Expiry<String, Item> {

    @Override
    public long expireAfterCreate(String key, Item value, long currentTime) {
        if (value == null) {
            return 0;
        }

        if (value.getExpireAtNano() == null) {
            return Long.MAX_VALUE;
        }

        return value.getExpireAtNano() - System.nanoTime();
    }

    @Override
    public long expireAfterUpdate(String key, Item value, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }

    @Override
    public long expireAfterRead(String key, Item value, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }
}
