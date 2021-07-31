package org.github.wangjunchao.mlcache.metrics;

import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:41
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class NoopMetrics implements IMetrics{
    @Override
    public void emitThroughput(String name, String method, Map<String, String> tags) {

    }

    @Override
    public void emitHit(String name, String method, Map<String, String> tags) {

    }

    @Override
    public void emitMiss(String name, String method, Map<String, String> tags) {

    }

    @Override
    public void emitError(String name, String method, Map<String, String> tags) {

    }

    @Override
    public void emitSuccess(String name, String method, Map<String, String> tags) {

    }

    @Override
    public void emitLatency(String name, String method, Map<String, String> tags) {

    }
}
