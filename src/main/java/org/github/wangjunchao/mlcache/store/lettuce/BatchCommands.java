package org.github.wangjunchao.mlcache.store.lettuce;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.batch.BatchExecutor;
import io.lettuce.core.dynamic.batch.BatchSize;
import io.lettuce.core.dynamic.batch.CommandBatching;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 07:45
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@BatchSize(100)
public interface BatchCommands extends Commands, BatchExecutor {
//    RedisFuture<byte[]> get(byte[] key);
//    RedisFuture<Set<byte[]>> smembers(byte[] key);
//    RedisFuture<List<byte[]>> lrange(byte[] key, long start, long end);
//    RedisFuture<Map<byte[], byte[]>> hgetall(byte[] key);
}
