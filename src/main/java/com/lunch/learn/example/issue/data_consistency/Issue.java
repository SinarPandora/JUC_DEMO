package com.lunch.learn.example.issue.data_consistency;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Author: sinar
 * 2020/11/29 21:38
 */
@SuppressWarnings("DuplicatedCode")
@Log
public class Issue {
    private static int count = 0;
    private static final int TASKS = 1000;

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < TASKS; i++) {
            pool.execute(() -> addOne());
        }
        if (pool.awaitTermination(3, TimeUnit.SECONDS)) {
            log.info("Result is " + count);
            pool.shutdown();
        }
    }

    private static void addOne() {
        count++;
    }
}
