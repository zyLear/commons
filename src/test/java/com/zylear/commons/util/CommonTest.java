package com.zylear.commons.util;

import com.zylear.commons.util.ConvertUtilTest.A;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiezongyu
 * @date 2021/5/7
 */
public class CommonTest {


    @Test
    public void outcomeTest() throws InterruptedException {

        final Exception[] outcome = {null};
        List<Integer> users = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            users.add(i);
        }
        ConcurrentHashMap concurrentHashMap;
        Thread thread1 = Thread.currentThread();

        ReentrantLock reentrantLock = null;
        Condition condition = reentrantLock.newCondition();
        condition.await();

        ThreadLocal threadLocal;



        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Executors.newCachedThreadPool();
        executorService.execute(null);
//        executorService.submit(null)
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


    //可见性测试
    @Test
    public void ArrayVolatileTest() throws InterruptedException {

        final AtomicReferenceArray<String> a = new AtomicReferenceArray<>(1);
        List<Integer> users = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            users.add(i);
        }
        Thread.sleep(2000);

        new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                try {
                    Thread.sleep(2000);
                    a.set(0, "xx");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 4) {
                    break;
                }
                System.out.println("1");
                System.out.println(a.get(0));
            }
        }).start();
        Thread.sleep(400);

        Thread thread = new Thread(() -> {
            while (true) {
                if (a.get(0) != null) {
                    System.out.println(a.get(0));
                    System.out.println(222);
                    break;
                }

            }
        });

        thread.start();
        thread.join();
    }

    class Food {
    }

    class Fruit extends Food {
        public int fruit;
    }

    class Apple extends Fruit {

    }

    class Orange extends Fruit {
    }

    @Test
    public void tTest() {

        List<Fruit> list = new ArrayList<>();
//        list.add(new Food());
        list.add(new Fruit());
        list.add(new Apple());

    }


    public <T extends Fruit> void extendsTest(T t) {
        //t表示Fruit的子类，所以可以使用fruit的属性
        t.fruit = 1;

        //在参数使用 ? extends Fruit表示这个是Fruit的子类，
        //子类无法确定，所以add所有的报错
        List<T> list = new ArrayList<>();
//        list.add(new Food());
//        list.add(new Fruit());
//        list.add(new Apple());

    }

    public void superTest() {

        //在参数使用 ? super Fruit表示Fruit的父类
        //父类的子类已经确定，所以可以add，但是获取的是Object，因为无法确定具体是哪一个父类
        List<? super Fruit> list = new ArrayList<>();
//        list.add(new Food());
        list.add(new Apple());
        list.add(new Fruit());

        Object object = list.get(1);

    }


    public void quickSort(int[] array, int[] array1,int m,int n ) {

        int index = m + n - 1;


        while (m >= 0 && n >= 0) {

            if (array[m] > array1[n]) {
                array[index] = array[m];
                m--;
            }else {
                array[index] = array1[n];
                n--;
            }
            index--;

        }

        if (m < 0) {
            while (n >= 0) {
                array[index] = array1[n];
                n--;
                index--;
            }
        }else {
            while (m >= 0) {
                array[index] = array[m];
                m--;
                index--;
            }
        }



    }


    @Test
    public void testTwo() {
        int[] arr = new int[3];
        for (int i = 0; i < getLength(arr); i++) {

        }
    }

    private int getLength(int[] arr) {
        System.out.println("one");
        return arr.length;
    }


}
