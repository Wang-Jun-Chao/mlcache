package org.github.wangjunchao.mlcache.metrics;

import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:38
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public interface IMetrics {
    void emitThroughput(String name, String method, Map<String, String> tags);

    void emitHit(String name, String method, Map<String, String> tags);

    void emitMiss(String name, String method, Map<String, String> tags);

    void emitError(String name, String method, Map<String, String> tags);

    void emitSuccess(String name, String method, Map<String, String> tags);

    void emitLatency(String name, String method, Map<String, String> tags);
}
