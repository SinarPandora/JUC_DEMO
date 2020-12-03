package com.lunch.learn.example.issue.data_consistency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.java.Log;

/**
 * Author: sinar
 * 2020/11/29 21:38
 */
@SuppressWarnings("DuplicatedCode")
@Log
public class Issue {
    private static AtomicInteger COUNT = new AtomicInteger(0);
    private static final int TASKS = 1000;

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < TASKS; i++) {
            pool.execute(() -> addOne());
        }
        Thread.sleep(3000);
        log.info("Result is " + COUNT);
        pool.shutdown();
    }

    private static void addOne() {
        // count ++
        COUNT.getAndIncrement();
    }
}
