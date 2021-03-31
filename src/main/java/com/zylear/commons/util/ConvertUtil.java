package com.zylear.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

/**
 * @author xiezongyu
 * @date 2021/1/22
 */
public class ConvertUtil {


//    public static <T, R> Page<R> convertPage(Page<T> beans, Class<R> clazz) {
//        return convertPage(beans, clazz, null);
//    }
//
//    public static <T, R> Page<R> convertPage(Page<T> page, Class<R> clazz, BiFunction<T, R, R> function) {
//        return page.map(bean -> convertBean(bean, clazz, function));
//    }

    public static <T, R> List<R> convertList(List<T> beans, Class<R> clazz) {
        return convertList(beans, clazz, null);
    }

    public static <T, R> List<R> convertList(List<T> beans, Class<R> clazz, BiFunction<T, R, R> function) {
        List<R> list = new ArrayList<>(beans.size());
        for (T bean : beans) {
            R item = convertBean(bean, clazz, function);
            list.add(item);
        }
        return list;
    }

    public static <T, R> R convertBean(T bean, Class<R> clazz, BiFunction<T, R, R> function) {
        R item = BeanUtils.instantiate(clazz);
        BeanUtils.copyProperties(bean, item);
        if (function != null) {
            item = function.apply(bean, item);
        }
        return item;
    }

    public static <T, R> R convertBean(T bean, Class<R> clazz) {
        return convertBean(bean, clazz, null);
    }


    @SafeVarargs
    public static <T, R> BiFunction<T, R, R> combination(BiFunction<? super T, ? super R, ? extends R>... functions) {
        return (t, r) -> {
            for (BiFunction<? super T, ? super R, ? extends R> function : functions) {
                if (function == null) {
                    continue;
                }
                r = function.apply(t, r);
            }
            return r;
        };
    }

    public static <T, R> BiFunction<T, R, R> getConverterFunction(Method method, Map map, Map<String, String> propertiesMap) {
        BiFunction<Object, Object, Object> function = (bean, item) -> {
            if (method == null || map == null || map.isEmpty()) {
                return item;
            }

            Object id = BeanUtil.invoke(method, bean);
            Object source = map.get(id);
            if (source == null) {
                return item;
            }
            for (Entry<String, String> entry : propertiesMap.entrySet()) {
                Method readMethod = BeanUtil.findReadMethod(source.getClass(), entry.getKey());
                Method writeMethod = BeanUtil.findWriteMethod(item.getClass(), entry.getValue());
                BeanUtil.invokeMethod(readMethod, source, writeMethod, item);
            }
            return item;
        };

        return (BiFunction<T, R, R>) function;
    }

    public static BigDecimal parseBigDecimal(String str) {
        return StringUtils.isEmpty(str) ? null : BigDecimal.valueOf(Double.parseDouble(str.trim()));
    }

    public static BigDecimal parseBigDecimal(String str, BigDecimal defaultValue) {
        return StringUtils.isEmpty(str) ? defaultValue : BigDecimal.valueOf(Double.parseDouble(str.trim()));
    }

    public static Integer parseInt(String str) {
        return StringUtils.isEmpty(str) ? null : Integer.parseInt(str.trim());
    }

    public static Integer parseInt(String str, Integer defaultValue) {
        return StringUtils.isEmpty(str) ? defaultValue : Integer.parseInt(str.trim());
    }

    public static Long parseLong(String str) {
        return StringUtils.isEmpty(str) ? null : Long.parseLong(str);
    }

}
