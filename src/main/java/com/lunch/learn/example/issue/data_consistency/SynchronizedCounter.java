package com.lunch.learn.example.issue.data_consistency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.java.Log;

/**
 * Author: sinar
 * 2020/11/29 21:38
 */
@Log
@SuppressWarnings("DuplicatedCode")
public class SynchronizedCounter {
    private static int count = 0;
    private static final int TASKS = 1000;

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < TASKS; i++) {
            pool.execute(() -> addOne());
        }
        Thread.sleep(3000);
        log.info("Result is " + count);
        pool.shutdown();
    }

    private synchronized static void addOne() {
        count++;
    }
}
