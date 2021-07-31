package org.github.wangjunchao.mlcache.value;

import java.time.Duration;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-07-31 10:49
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public interface Consts {
    /**
     * 当 key 存在但没有设置剩余生存时间时使用
     */
    Duration UNSET = Duration.ofSeconds(-1);
    /**
     * 当 key 不存在时使用
     */
    Duration NOT_EXIST = Duration.ofSeconds(-1);
    /**
     * 表示不应该使用当前值
     */
    Duration USELESS = Duration.ofSeconds(-1);

    /**
     * 缓存 key 的值不存时，可使用此默值占位，防止缓存穿透
     */
    String NOT_EXIT_VALUE = "~!@#$%^&*({[NOT_EXIST]})*&^%$#@!~";
}
