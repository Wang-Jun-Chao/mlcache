package org.github.wangjunchao.mlcache.load;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 从持久化存储中加载数据
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 11:03
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public interface ILoad<K, V> {
    /**
     * 加载多条数据，返回key-value 对象，如果没有找到对应的值，不需要将key放入到返回的map中
     * @param keys 需要批量查询的值
     * @return
     */
    Map<K, V> load(List<K> keys);
}
