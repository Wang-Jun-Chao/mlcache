package org.github.wangjunchao.mlcache.key;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:10
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class MD5KeyCreator implements IKeyCreator{
    @Override
    public <T> String getString(T t) {
        return null;
    }

    @Override
    public <T> List<T> getStrings(List<T> ts) {
        return null;
    }

    @Override
    public <T> T getKey(Class<T> clazz, String s) {
        return null;
    }

    @Override
    public <T> List<T> getKeys(Class<T> clazz, List<String> ks) {
        return null;
    }
}
