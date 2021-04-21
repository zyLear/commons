package com.zylear.commons.util;

import com.zylear.commons.annotation.Mapped;
import com.zylear.commons.annotation.Mappeds;
import lombok.Data;
import org.junit.Test;

public class ConvertUtilTest {


    @Test
    public void test() {


        System.out.println(ConvertUtil.convertBean(new A(), B.class));
        System.out.println(ConvertUtil.convertBean(new A(), B.class));
    }

    @Data
    public static class A {
        private String find = "ss";

        private Integer find1 = 1;

    }

    @Data
    public static class B {
        @Mappeds(@Mapped(clazz = A.class, field = "find"))
        private String name;

        @Mapped(clazz = A.class, field = "find1")
        private String name1;
    }

}