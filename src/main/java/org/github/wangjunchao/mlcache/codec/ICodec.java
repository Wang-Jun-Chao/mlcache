package org.github.wangjunchao.mlcache.codec;

import com.google.common.collect.Lists;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2021-08-01 10:44
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public interface ICodec {

    class Helper {
        private static String bytesToString(byte[] bytes, Charset charset) {
            if (bytes == null) {
                return null;
            }
            return new String(bytes, charset);
        }

        private static byte[] stringToBytes(String value, Charset charset) {
            if (value == null) {
                return null;
            }

            return value.getBytes(charset);
        }
    }

    /**
     * 获取字符编码
     *
     * @return
     */
    default Charset getCharset() {
        return StandardCharsets.UTF_8;
    }


    <T> byte[] getBytes(T t);

    default <T> String getString(T t) {
        byte[] bytes = getBytes(t);
        return Helper.bytesToString(bytes, getCharset());
    }


    default List<byte[]> getBytes(List<?> e) {
        if (e == null) {
            return null;
        }

        List<byte[]> result = Lists.newArrayListWithCapacity(e.size());
        e.forEach(item -> result.add(getBytes(item)));

        return result;
    }

    default List<String> getString(List<?> e) {
        List<byte[]> list = getBytes(e);
        if (list == null) {
            return null;
        }

        List<String> result = Lists.newLinkedList();
        list.forEach(item -> {
            result.add(Helper.bytesToString(item, getCharset()));
        });

        return result;
    }


    <T> T getObject(Class<T> clazz, byte[] bytes);

    default <T> T getObject(Class<T> clazz, String s) {
        byte[] bytes = Helper.stringToBytes(s, getCharset());
        if (bytes == null) {
            return null;
        }
        return getObject(clazz, bytes);
    }

    default List<?> getObjects(List<Class<?>> classList, List<byte[]> bytesList) {

        if (classList == null && bytesList == null
                || classList.size() == 0 && classList.size() == bytesList.size()) {
            return Lists.newArrayList();
        }

        if (classList == null) {
            throw new IllegalArgumentException("classList is empty");
        }

        if (bytesList == null) {
            throw new IllegalArgumentException("bytesList is empty");
        }

        if (classList.size() != bytesList.size()) {
            throw new IllegalArgumentException(
                    String.format("classList.size()(%S) != bytesList.size()(%s)",
                            classList.size(), bytesList.size()));
        }

        List<Object> result = Lists.newArrayListWithCapacity(classList.size());
        for (int i = 0; i < classList.size(); i++) {
            Object o = getObject(classList.get(i), bytesList.get(i));
            result.add(o);
        }

        return result;
    }

    default List<?> getObjects(List<Class<?>> clazz, String... items) {
        List<byte[]> bytesList = Lists.newArrayList();

        if (items != null) {
            for (String s : items) {
                bytesList.add(Helper.stringToBytes(s, getCharset()));
            }
        }

        return getObjects(clazz, bytesList);
    }
}
