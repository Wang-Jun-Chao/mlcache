package org.github.wangjunchao.mlcache.key;

import java.util.List;

/**
 * <pre>
 * 缓存key生成器
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:06
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public interface IKeyCreator {
    <T> String getString(T t);

    <T> List<T> getStrings(List<T> ts);

    <T> T getKey(Class<T> clazz, String s);

    <T> List<T> getKeys(Class<T> clazz, List<String> ks);
}
