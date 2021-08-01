package org.github.wangjunchao.mlcache.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 11:47
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
class JSONCodecTest {
    private JSONCodec codec;
    @BeforeEach
    void setUp() {
        codec = new JSONCodec(new ObjectMapper());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBytes() {
        byte[] data = "wangjunchao".getBytes(StandardCharsets.UTF_8);
        byte[] bytes = codec.getBytes(data);
        byte[] result = codec.getObject(byte[].class, bytes);
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(result));
        Assertions.assertArrayEquals(data, result);
    }

    @Test
    void getObject() {
    }
}