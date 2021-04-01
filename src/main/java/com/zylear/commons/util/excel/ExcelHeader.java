package com.zylear.commons.util.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelHeader implements Comparable<ExcelHeader> {

    /**
     * excel的标题名称
     */
    private String title;

    /**
     * 每一个标题的顺序
     */
    private int order;

    /**
     * 注解域
     */
    private String fieldName;


    private Field field;



    @Override
    public int compareTo(ExcelHeader o) {
        return Integer.compare(order, o.getOrder());
    }


}
