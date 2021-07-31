package org.github.wangjunchao.mlcache.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.github.wangjunchao.mlcache.key.IKeyCreator;
import org.github.wangjunchao.mlcache.metrics.IMetrics;
import org.github.wangjunchao.mlcache.value.IValueCreator;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:36
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {
    private String name;
    private String type;
    private IKeyCreator keyCreator;
    private IValueCreator valueCreator;
    private Options options;
    private IMetrics metrics;
}
