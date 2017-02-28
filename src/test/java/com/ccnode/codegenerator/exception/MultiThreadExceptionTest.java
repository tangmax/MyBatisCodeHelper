package com.ccnode.codegenerator.exception;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author bruce.ge
 * @Date 2017/2/22
 * @Description
 */
public class MultiThreadExceptionTest {
    @Test
    public void test(){
        ExecutorService service = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            service.submit((Runnable) () -> {
                try {
                    System.out.println("start thread");
                    throw new RuntimeException("hello");
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("haha");
    }
}
