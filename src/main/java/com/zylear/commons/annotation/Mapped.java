package com.zylear.commons.annotation;

import java.lang.annotation.*;

/**
 *
 * @author xiezongyu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(Mappeds.class)
public @interface Mapped {

    /**
     * class
     */
    Class clazz();

    /**
     * 在excel的顺序
     */
    String field();
}
