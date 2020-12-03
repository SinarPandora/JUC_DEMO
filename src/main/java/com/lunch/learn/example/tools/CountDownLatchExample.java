package com.lunch.learn.example.tools;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.java.Log;

/**
 * Author: sinar
 * 2020/12/2 22:09
 */
@Log
public class CountDownLatchExample {

    private static final Random RANDOM = new SecureRandom();
    private static final int TASK_NUMS = 100;
    private static final int POOL_SIZE = 10;
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(TASK_NUMS);

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        for (int i = 0; i < TASK_NUMS; i++) {
            final int taskId = i;
            pool.submit(() -> updateData(taskId));
        }
        COUNT_DOWN_LATCH.await();
        pool.shutdownNow();
        log.info("DONE!");
    }

    private static boolean updateData(int taskId) throws InterruptedException {
        log.info("Prepare for update! Task id is " + taskId);
        Thread.sleep(RANDOM.nextInt(2) * 1000);
        log.info("Update done! Task id is " + taskId);
        COUNT_DOWN_LATCH.countDown();
        return true;
    }
}
