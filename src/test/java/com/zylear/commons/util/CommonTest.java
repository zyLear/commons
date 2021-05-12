package com.zylear.commons.util;

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
        users.parallelStream().forEach(user -> {
            for (int i = 0; i < 20000; i++) {
                try {
                    if (Thread.currentThread() != thread1) {
                        Thread.sleep(2000);
                        throw new RuntimeException();
                    }
                } catch (Exception e) {
                    if (outcome[0] == null) {
                        outcome[0] = e;
                    }
                    break;
                }
            }
        });
//        new Thread(() -> {
//            for (int i = 0; i < 20000; i++) {
//                try {
//                    Thread.sleep(2000);
//                    throw new RuntimeException();
//                } catch (Exception e) {
//                    if (outcome[0] == null) {
//                        outcome[0] = e;
//                    }
//                    break;
//                }
//            }
//        }).start();

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

}
