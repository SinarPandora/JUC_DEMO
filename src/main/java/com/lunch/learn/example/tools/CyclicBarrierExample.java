package com.lunch.learn.example.tools;

import lombok.extern.java.Log;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Author: sinar
 * 2020/12/2 22:09
 */
@Log
public class CyclicBarrierExample {

    private static final Random RANDOM = new SecureRandom();
    private static final int TASK_NUMS = 100;
    private static final int POOL_SIZE = 10;
    private static final int GROUP_SIZE = 5;
    private static final CyclicBarrier BARRIER = new CyclicBarrier(GROUP_SIZE);

    public static void main(String[] args) throws InterruptedException {
        // Maybe here is a global pool managed by Spring
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        for (int i = 0; i < TASK_NUMS; i++) {
            final var taskId = i;
            Thread.sleep(1000);
            pool.submit(() -> updateData(taskId));
        }
        pool.shutdown();
        log.info("DONE!");
    }

    private static boolean updateData(int taskId) throws InterruptedException, BrokenBarrierException {
        log.info("Prepare for update! Task id is " + taskId);
        Thread.sleep(RANDOM.nextInt(3) * 1000);
        log.info("Ready for update! Task id is " + taskId);
        BARRIER.await();
        log.info("Update done! Task id is " + taskId);
        return true;
    }
}
