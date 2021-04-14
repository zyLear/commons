package com.zylear.commons.util;

import com.zylear.commons.annotation.Mapped;
import com.zylear.commons.annotation.Mappeds;
import com.zylear.commons.util.excel.ExcelField;
import com.zylear.commons.util.excel.ExcelHeader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        Map<String, String> propertiesMap = getPropertiesMap(bean.getClass(), clazz);
        invokePropertiesMap(bean, item, propertiesMap);
        if (function != null) {
            item = function.apply(bean, item);
        }
        return item;
    }

    private static <R> Map<String, String> getPropertiesMap(Class<?> source, Class<R> target) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clz = target; clz != Object.class; clz = clz.getSuperclass()) {
            fields.addAll(Arrays.asList(clz.getDeclaredFields()));
        }
        Map<String, String> result = new HashMap<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Mapped.class)) {
                Mapped mapped = field.getAnnotation(Mapped.class);
                if (source.equals(mapped.clazz())) {
                    result.put(mapped.field(), field.getName());
                }

            } else if (field.isAnnotationPresent(Mappeds.class)) {
                Mappeds mappeds = field.getAnnotation(Mappeds.class);
                Mapped[] maps = mappeds.value();
                for (Mapped mapped : maps) {
                    if (source.equals(mapped.clazz())) {
                        result.put(mapped.field(), field.getName());
                    }
                }
            }
        }
        return result;
    }

    private static <T, R> void invokePropertiesMap(T bean, R item, Map<String, String> propertiesMap) {
        for (Entry<String, String> entry : propertiesMap.entrySet()) {
            Method readMethod = BeanUtil.findReadMethod(bean.getClass(), entry.getKey());
            Method writeMethod = BeanUtil.findWriteMethod(item.getClass(), entry.getValue());
            if (readMethod != null && writeMethod != null &&
                    ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                BeanUtil.invokeMethod(readMethod, bean, writeMethod, item);
            }
        }
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
            invokePropertiesMap(source, item, propertiesMap);
            return item;
        };

        return (BiFunction<T, R, R>) function;
    }


    public <T, R, V> BiFunction<T, R, R> convertFunction(String keyPropertyName,
                                                         String idPropertyName,
                                                         List<T> beans,
                                                         Function<Collection<Long>, List<V>> function,
                                                         Map<String, String> propertiesMap) {

        Method method = BeanUtil.getSpecifiedReadMethod(beans, keyPropertyName);
        Collection<Long> ids = BeanUtil.getSpecifiedMethodResult(beans, method);
        List<V> products = function.apply(ids);
        Method readIdMethod = BeanUtil.getSpecifiedReadMethod(products, idPropertyName);
        Map<Object, Object> orgMap = products.stream().collect(Collectors.toMap(y -> BeanUtil.invoke(readIdMethod, y), v -> v));

        return ConvertUtil.getConverterFunction(method, orgMap, propertiesMap);
    }

}
