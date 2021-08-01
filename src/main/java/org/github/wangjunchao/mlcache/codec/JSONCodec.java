package org.github.wangjunchao.mlcache.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 10:50
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JSONCodec implements ICodec {

    private ObjectMapper mapper;

    @Override
    public <T> byte[] getBytes(T t) {
        try {
            return mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getObject(Class<T> clazz, byte[] bytes) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
