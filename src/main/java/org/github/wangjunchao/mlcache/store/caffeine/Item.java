package org.github.wangjunchao.mlcache.store.caffeine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 14:24
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
class Item {
    /**
     * 数据部分
     */
    private byte[] data;
    /**
     * 过期时间
     */
    private Long expireAtNano;
}
