package com.zylear.commons.util;

import com.zylear.commons.util.ConvertUtilTest.A;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiezongyu
 * @date 2021/5/7
 */
public class CommonTest {

    @Test
    public void superTest() throws InterruptedException {

        final Exception[] outcome = {null};
        List<Integer> users = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            users.add(i);
        }
        Thread thread1 = Thread.currentThread();
//        users.parallelStream().forEach(user -> {
//            for (int i = 0; i < 20000; i++) {
//                try {
//                    if (Thread.currentThread() != thread1) {
//                        Thread.sleep(2000);
//                        throw new RuntimeException();
//                    }
//                } catch (Exception e) {
//                    if (outcome[0] == null) {
//                        outcome[0] = e;
//                    }
//                    break;
//                }
//            }
//        });
        new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                try {
                    Thread.sleep(2000);
                    throw new RuntimeException();
                } catch (Exception e) {
                    if (outcome[0] == null) {
                        outcome[0] = e;
                    }
                    break;
                }
            }
            System.out.println("1");
            System.out.println(outcome[0].getMessage());
        }).start();

        Thread thread = new Thread(() -> {
            while (true) {
                if (outcome[0] != null) {
                    outcome[0].printStackTrace();
                    System.out.println(222);
                    break;
                }
            }
        });

        thread.start();
        thread.join();
    }

    @Test
    public void threadPoolTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        try {
            executorService.execute(() -> {
                System.out.println(1);
                throw new RuntimeException("ssss");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(10000);

    }


    //数组里面的值可见性测试
    @Test
    public void arrayVolatileTest() throws InterruptedException {

        final String[] outcome = {null};
        List<Integer> users = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            users.add(i);
        }
        Thread.sleep(2000);

//        users.parallelStream()
        new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                try {
                    Thread.sleep(2000);
                    throw new RuntimeException("error");
                } catch (Exception e) {
                    outcome[0] = "gg";
                    break;
                }
            }
            System.out.println("1");
            System.out.println(outcome[0]);
        }).start();

        Thread thread = new Thread(() -> {
            while (true) {
                if (outcome[0] != null) {
                    System.out.println(outcome[0]);
                    System.out.println(222);
                    break;
                }
            }
        });

        thread.start();
        thread.join();
    }


    //对象属性里面的值可见性测试
    @Test
    public void ObjectFileVolatileTest() throws InterruptedException {

        final A a = new A();
        List<Integer> users = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            users.add(i);
        }
        Thread.sleep(2000);

        new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                try {
                    Thread.sleep(2000);
                    a.setGg("xx");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 4) {
                    break;
                }
                System.out.println("1");
                System.out.println(a.getGg());
            }
        }).start();
        Thread.sleep(400);

        Thread thread = new Thread(() -> {
            while (true) {
                if (a.getGg() != null) {
                    System.out.println(a.getGg());
                    System.out.println(222);
                    break;
                }

                //sleep  system.out.print 都可能有空刷新缓存 涉及上下文切换的场景

//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(1);
            }
        });

        thread.start();
        thread.join();
    }

}
