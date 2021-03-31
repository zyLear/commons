package com.zylear.commons.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author xiezongyu
 * @date 2021/2/22
 */
public class PagingUtil {

    public static <R> void paging(Long maxId, BiFunction<Long, Long, List<R>> biFunction, Consumer<R> consumer) {

        paging(0L, 1000, maxId, biFunction, consumer);

    }

    /**
     *
     * @param startExclude 开始id
     * @param size 每页大小
     * @param maxId 结束id
     * @param biFunction(startExclude, endInclude) 返回查找的List
     * @param consumer 处理逻辑
     * @param <R> 处理对象类型
     */
    public static <R> void paging(Long startExclude, Integer size, Long maxId,
                                  BiFunction<Long, Long, List<R>> biFunction, Consumer<R> consumer) {

        long endInclude = startExclude + size;

        while (startExclude < maxId) {
            List<R> list = biFunction.apply(startExclude, endInclude);

            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(consumer);
            }

            startExclude = endInclude;
            endInclude = startExclude + size;
        }


    }

    public static void main(String[] args) {
        paging(0L, 5, 50L, (startExclude, endInclude) -> {
            ArrayList<Long> objects = new ArrayList<>();
            for (Long i = startExclude + 1; i <= endInclude; i++) {
                objects.add(i);
            }
            return objects;
        }, System.out::println);
    }

}
