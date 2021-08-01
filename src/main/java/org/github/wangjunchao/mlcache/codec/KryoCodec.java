package org.github.wangjunchao.mlcache.codec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 10:57
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class KryoCodec implements ICodec{
    @Override
    public <T> byte[] getBytes(T t) {
        return new byte[0];
    }

    @Override
    public <T> T getObject(Class<T> clazz, byte[] bytes) {
        return null;
    }
}
