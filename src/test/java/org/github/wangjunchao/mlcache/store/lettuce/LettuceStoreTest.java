package org.github.wangjunchao.mlcache.store.lettuce;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.github.wangjunchao.mlcache.codec.JSONCodec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 09:47
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
class LettuceStoreTest {
    static RedisClient client;
    static StatefulRedisConnection<String, String> connect;
    @BeforeAll
    static void beforeAll() {
        RedisURI uri = RedisURI.create("192.168.241.129", 6379);
        uri.setPassword("123456");
        client = RedisClient.create(uri);
        client.setDefaultTimeout(Duration.ofSeconds(2));
        connect = client.connect();
    }

    @AfterAll
    static void afterAll() {
        client.shutdown();
    }


    @Test
    void setnx() {
        LettuceStore store = LettuceStore.builder()
                .connect(connect)
                .keyCodec(new JSONCodec(new ObjectMapper()))
                .valueCodec(new JSONCodec(new ObjectMapper()))
                .build();

        String s = "wangjunchao";
        store.del(s);
        store.setnx(s, s.getBytes(StandardCharsets.UTF_8), Duration.ofSeconds(5));
        byte[] result = store.get(s);

        Assertions.assertEquals(s, new String(result, StandardCharsets.UTF_8));
    }
}