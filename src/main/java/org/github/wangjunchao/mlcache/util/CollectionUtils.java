package org.github.wangjunchao.mlcache.util;

import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 14:44
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class CollectionUtils {
    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(Map m) {
        return m == null || m.isEmpty();
    }

    public static <T> boolean isEmpty(T[] ts) {
        return ts == null || ts.length < 1;
    }

    public static boolean isEmpty(Table t) {
        return t == null || t.isEmpty();
    }
}
