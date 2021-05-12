

package com.zylear.commons.util;


import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanUtil {


    public static <T, R> Collection<R> getSpecifiedMethodResult(List<T> beans, Method method) {
        if (method == null) {
            return Collections.emptyList();
        }
        return beans.stream().map(t1 -> (R) invoke(method, t1)).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static <T> Method getSpecifiedReadMethod(List<T> beans, String name) {
        if (CollectionUtils.isEmpty(beans)) {
            return null;
        }
        T t = beans.get(0);
        return findReadMethod(t.getClass(), name);
    }

    @SneakyThrows
    public static Object getField(Field field, Object item) {
        return field.get(item);
    }

    @SneakyThrows
    public static <R> R invoke(Method method, Object item, Object... params) {
        return (R) method.invoke(item, params);
    }

    public static void invokeMethod(Method read, Object source, Method write, Object target) {
        if (read != null && write != null &&
                ClassUtils.isAssignable(write.getParameterTypes()[0], read.getReturnType())) {
            invoke(write, target, (Object) invoke(read, source));
        }
    }

    public static <R> Map<String, Method> findWriteMethodMap(Class<R> clazz, Collection<String> strings) {
        Map<String, Method> methods = new HashMap<>(strings.size());
        for (String name : strings) {
            Method writeMethod = findWriteMethod(clazz, name);
            if (writeMethod != null) {
                methods.put(name, writeMethod);
            }
        }
        return methods;
    }

    public static <R> Method findWriteMethod(Class<R> clazz, String name) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, name);
        if (propertyDescriptor != null) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null) {
                return writeMethod;
            }
        }
        return null;
    }

    public static <R> Method findReadMethod(Class<R> clazz, String name) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, name);
        if (propertyDescriptor != null) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                return readMethod;
            }
        }
        return null;
    }

    public static <R> Map<String, Method> findReadMethodMap(Class<R> clazz, Collection<String> strings) {
        Map<String, Method> methods = new HashMap<>(strings.size());
        for (String name : strings) {
            Method readMethod = findReadMethod(clazz, name);
            if (readMethod != null) {
                methods.put(name, readMethod);
            }

        }
        return methods;
    }


}
