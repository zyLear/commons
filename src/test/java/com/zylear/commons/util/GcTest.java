package com.zylear.commons.util;

import org.junit.Test;

/**
 * @author xiezongyu
 * @date 2021/5/7
 */
public class GcTest {


    @Test
    public void testGc() {
        int _1m = 1024 * 1024;
        byte[] data = new byte[_1m];
        // 将data置为null即让它成为垃圾
        data = null;
        // 通知垃圾回收器回收垃圾
        System.gc();
    }

    @Test
    public void TestByte() {
        int a = 128;
        Byte b = (byte) a;
        System.out.println(b);
        System.out.println((byte) 129);
        System.out.println((byte) 300);
    }
}


// class forName