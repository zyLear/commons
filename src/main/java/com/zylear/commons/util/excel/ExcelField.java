package com.zylear.commons.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来在对象的属性上加入的annotation，通过该annotation说明某个属性所对应的标题
 * @author xiezongyu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    /**
     * 属性的标题名称
     */
    String title();

    /**
     * 在excel的顺序
     */
    int order() default 9999;
}
