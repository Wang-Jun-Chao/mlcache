package org.github.wangjunchao.mlcache.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.github.wangjunchao.mlcache.key.IKeyCreator;
import org.github.wangjunchao.mlcache.metrics.IMetrics;
import org.github.wangjunchao.mlcache.value.IValueCreator;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:57
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractStore implements IStore {
    protected String name;
    protected String type;
    protected IKeyCreator keyCreator;
    protected IValueCreator valueCreator;
    protected IMetrics metrics;
}
