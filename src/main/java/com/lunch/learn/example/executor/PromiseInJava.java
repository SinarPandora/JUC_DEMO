package com.lunch.learn.example.executor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.java.Log;

/**
 * Author: sinar
 * 2020/12/2 23:42
 */
@Log
public class PromiseInJava {

    private static final int POOL_SIZE = 20;
    private static final int TASK_NUM = 20;

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService singlePool = Executors.newSingleThreadExecutor();
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < TASK_NUM; i++) {
            final int id = i;
            CompletableFuture
                    .supplyAsync(() -> hello(id), pool)
                    .thenAcceptAsync(idx -> {
                        System.out.println(Thread.currentThread().getName());
                        log.info("Hello YCY! I'm " + idx);
                    }, singlePool);
        }

        // Just block for demo
        Thread.sleep(100_000);
    }

    private static int hello(int id) {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return id;
    }
}
