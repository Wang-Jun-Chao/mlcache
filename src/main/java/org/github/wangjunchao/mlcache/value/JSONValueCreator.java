package org.github.wangjunchao.mlcache.value;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:21
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class JSONValueCreator implements IValueCreator{
    @Override
    public <T> byte[] getValue(T t) {
        return new byte[0];
    }

    @Override
    public <T> byte[] getValues(List<T> ts) {
        return new byte[0];
    }

    @Override
    public <T> T getObject(Class<T> clazz, byte[] bytes) {
        return null;
    }

    @Override
    public <T> List<T> getObjects(Class<T> clazz, List<byte[]> bytes) {
        return null;
    }
}
