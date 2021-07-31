package org.github.wangjunchao.mlcache.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 10:53
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Item {
    /**
     * 值的二进制表示，比如 json, xml, protobuf 等
     */
    private byte[] value;
    /**
     * Key 对应的值是否存在
     */
    private boolean exist;
}
