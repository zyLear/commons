package com.zylear.commons.util;

import com.zylear.commons.util.ConvertUtilTest.A;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReferenceArray;

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


    //?????????????????????????????????
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


    //???????????????????????????????????????
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

                //sleep  system.out.print ??????????????????????????? ??????????????????????????????

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


    //???????????????
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
        //t??????Fruit??????????????????????????????fruit?????????
        t.fruit = 1;

        //??????????????? ? extends Fruit???????????????Fruit????????????
        //???????????????????????????add???????????????
        List<T> list = new ArrayList<>();
//        list.add(new Food());
//        list.add(new Fruit());
//        list.add(new Apple());

    }

    public void superTest() {

        //??????????????? ? super Fruit??????Fruit?????????
        //??????????????????????????????????????????add?????????????????????Object?????????????????????????????????????????????
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


}
