package com.zylear.commons.util;

import com.zylear.commons.annotation.Mapped;
import com.zylear.commons.annotation.Mappeds;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.lang.ref.*;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

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

        private String gg;

    }

    @Data
    public static class B {
        @Mappeds(@Mapped(clazz = A.class, field = "find"))
        private String name;

        @Mapped(clazz = A.class, field = "find1")
        private String name1;
    }

    @SneakyThrows
    @Test
    public void weakReferenceTest() {
//        WeakReference<Integer> integer = new WeakReference<>(new Integer(1));
//        System.out.println(integer.get());
//        System.gc();
//        Thread.sleep(5000L);
//        System.out.println(integer.get());


        Executors.newFixedThreadPool(1);
        Executors.newCachedThreadPool();
        Executors.newSingleThreadScheduledExecutor();


        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        ReferenceQueue<Object> queue = new ReferenceQueue<>();

        HashMap<WeakReference<Integer>, String> map = new HashMap<>();
        WeakReference<Integer> weak = new WeakReference<>(new Integer(1), queue);
        map.put(weak, "1");
        map.put(new WeakReference<>(new Integer(2), queue), "2");
        System.out.println(map.size());
        System.out.println(map.get(weak));
        System.out.println(weak.get());
        System.gc();
        System.out.println(map.get(weak));
        System.out.println(map.size());
        System.out.println(weak.get());
        System.out.println();
        Reference reference;
        while ((reference = queue.poll()) != null) {
            System.out.println(reference.get());
        }


    }

    @SneakyThrows
    @Test
    public void softReferenceTest() {
        SoftReference<Integer> softReference = new SoftReference<>(new Integer(1));
        PhantomReference<Object> phantomReference = new PhantomReference<>(new Object(), new ReferenceQueue<>());
    }

}