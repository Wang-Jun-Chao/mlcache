package org.github.wangjunchao.mlcache.value;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:14
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public interface IValueCreator {
    <T> byte[] getValue(T t);

    <T> byte[] getValues(List<T> ts);

    <T> T getObject(Class<T> clazz, byte[] bytes);

    <T> List<T> getObjects(Class<T> clazz, List<byte[]> bytes);
}
