package com.zylear.commons.util;

import com.zylear.commons.annotation.Mapped;
import com.zylear.commons.annotation.Mappeds;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiezongyu
 * @date 2021/1/22
 */
public class ConvertUtil {

    private static ConcurrentHashMap<String, List<Pair<String, String>>> classPropertiesCache = new ConcurrentHashMap<>();


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
        List<Pair<String, String>> propertiesMap = getPropertiesMap(bean.getClass(), clazz);
        invokePropertiesMap(bean, item, propertiesMap);
        if (function != null) {
            item = function.apply(bean, item);
        }
        return item;
    }

    private static <R> List<Pair<String, String>> getPropertiesMap(Class<?> source, Class<R> target) {
        String key = source.getCanonicalName() + "::" + target.getCanonicalName();
        List<Pair<String, String>> cache = classPropertiesCache.get(key);
        if (cache != null) {
            return cache;
        }
        List<Pair<String, String>> result = new ArrayList<>();
        for (Class<?> clz = target; clz != Object.class; clz = clz.getSuperclass()) {
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Mapped.class)) {
                    Mapped mapped = field.getAnnotation(Mapped.class);
                    if (source.equals(mapped.clazz())) {
                        result.add(Pair.of(mapped.field(), field.getName()));
                    }
                } else if (field.isAnnotationPresent(Mappeds.class)) {
                    Mappeds mappeds = field.getAnnotation(Mappeds.class);
                    Mapped[] maps = mappeds.value();
                    for (Mapped mapped : maps) {
                        if (source.equals(mapped.clazz())) {
                            result.add(Pair.of(mapped.field(), field.getName()));
                        }
                    }
                }
            }
        }
        classPropertiesCache.put(key, result);
        return result;
    }

    private static <T, R> void invokePropertiesMap(T bean, R item, List<Pair<String, String>> propertiesMap) {
        for (Pair<String, String> pair : propertiesMap) {
            invokePropertiesMap(bean, item, pair.getFirst(), pair.getSecond());
        }
    }

    private static <T, R> void invokePropertiesMap(T bean, R item, String first, String second) {
        Method readMethod = BeanUtil.findReadMethod(bean.getClass(), first);
        Method writeMethod = BeanUtil.findWriteMethod(item.getClass(), second);
        BeanUtil.invokeMethod(readMethod, bean, writeMethod, item);
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

    public static <T, R, P, V> BiFunction<T, R, R> getConverterFunction(Method method, Map<P, V> map, Map<String, String> propertiesMap) {
        return (bean, item) -> {
            if (method == null || map == null || map.isEmpty()) {
                return item;
            }

            P id = BeanUtil.invoke(method, bean);
            V source = map.get(id);
            if (source == null) {
                return item;
            }
            propertiesMap.forEach((k, v) -> invokePropertiesMap(source, item, k, v));
            return item;
        };
    }


    public static <T, R, V, P> BiFunction<T, R, R> convertFunction(String keyPropertyName,
                                                                   String idPropertyName,
                                                                   List<T> beans,
                                                                   Function<Collection<P>, List<V>> function,
                                                                   Map<String, String> propertiesMap) {

        Method method = BeanUtil.getSpecifiedReadMethod(beans, keyPropertyName);
        Collection<P> ids = BeanUtil.getSpecifiedMethodResult(beans, method);
        List<V> products = function.apply(ids);
        Method readIdMethod = BeanUtil.getSpecifiedReadMethod(products, idPropertyName);
        Map<P, V> orgMap = products.stream().collect(Collectors.toMap(y -> BeanUtil.invoke(readIdMethod, y), v -> v));

        return ConvertUtil.getConverterFunction(method, orgMap, propertiesMap);
    }

    //usage
    public static final Map<String, String> PROPERTIES_MAP = new HashMap<String, String>() {
        {
            put("type", "taskType");
        }
    };

    public <T, R> BiFunction<T, R, R> convertFunction(List<T> beans) {
        return ConvertUtil.convertFunction("userId", "id", beans,
                (Function<Collection<String>, List<Object>>) ids -> new ArrayList<>()
                , PROPERTIES_MAP);
    }

}
