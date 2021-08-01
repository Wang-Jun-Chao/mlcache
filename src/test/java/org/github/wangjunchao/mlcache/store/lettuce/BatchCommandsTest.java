package org.github.wangjunchao.mlcache.store.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.lettuce.core.resource.DefaultClientResources;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 07:48
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
class BatchCommandsTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    public void testBatchCommands() {
//        // 创建客户端
//        RedisClient client = RedisClient.create(DefaultClientResources.create(), "redis://192.168.241.129:6973");
//
//        // service 中持有 factory 实例，只创建一次。第二个参数表示 key 和 value 使用 byte[] 编解码
//        RedisCommandFactory factory = new RedisCommandFactory(client.connect(), Arrays.asList(ByteArrayCodec.INSTANCE, ByteArrayCodec.INSTANCE));
//
//        // 使用的地方，创建一个查询实例代理类调用命令，最后刷入命令
//        List<RedisFuture<?>> futures = new ArrayList<>();
//        BatchCommands batchQuery = factory.getCommands(BatchCommands.class);
//        for (RedisMetaGroup redisMetaGroup : redisMetaGroups) {
//            // 业务逻辑，循环调用多个 key 并将结果保存到 futures 结果中
//            appendCommand(redisMetaGroup, futures, batchQuery);
//        }
//
//// 异步命令调用完成后执行 flush 批量执行，此时命令才会发送给 Redis 服务端
//        batchQuery.flush();
    }
}