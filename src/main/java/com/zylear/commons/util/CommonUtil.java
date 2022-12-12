/*
 * Copyright (c) 2018, Magnum Research Ltd. All rights reserved.
 *
 * This program and the accompanying materials (“Program”)
 * whether on any media or in any form,
 * are exclusive property of Magnum Research Limited (“Magnum”).
 * Without prior written authorization from Magnum,
 * any person shall not reproduce, modify, summarize,
 * reverse-engineer, decompile or disassemble the Program,
 * or disclose any part of this Program to any other person.
 *
 * Magnum reserves all rights not expressly stated herein.
 *  
 *
 */

package com.zylear.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
public class CommonUtil {


    public static Runnable logRunnable(Runnable runnable, Collection<String> keyMap) {
        Map<String, String> map = getMdcValue(keyMap);

        Thread thread = Thread.currentThread();
        return () -> {
            if (thread == Thread.currentThread()) {
                runnable.run();
            } else {
                try {
                    try {
                        map.forEach(MDC::put);
                        runnable.run();
                    } finally {
                        map.keySet().forEach(MDC::remove);
                    }
                } catch (Exception e) {
                    log.error("logRunnable error. ", e);
                    throw e;
                }
            }

        };
    }

    private static Map<String, String> getMdcValue() {
        return MDC.getCopyOfContextMap();
    }

    private static Map<String, String> getMdcValue(Collection<String> keyMap) {
        Map<String, String> map = new HashMap<>(keyMap.size());
        for (String key : keyMap) {
            String value = MDC.get(key);
            if (StringUtils.isNotEmpty(value)) {
                map.put(key, value);
            }
        }
        return map;
    }

    public static <T> Consumer<T> logConsumer(Consumer<T> consumer, Collection<String> keyMap) {

        Map<String, String> map = getMdcValue(keyMap);
        Thread thread = Thread.currentThread();
        return t -> {
            if (thread == Thread.currentThread()) {
                consumer.accept(t);
            } else {
                try {
                    try {
                        map.forEach(MDC::put);
                        consumer.accept(t);
                    } finally {
                        map.keySet().forEach(MDC::remove);
                    }
                } catch (Exception e) {
                    log.error("LogRunnable error. ", e);
                    throw e;
                }
            }
        };
    }

}
