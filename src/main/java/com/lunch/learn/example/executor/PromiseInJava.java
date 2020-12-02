package com.lunch.learn.example.executor;

import lombok.extern.java.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: sinar
 * 2020/12/2 23:42
 */
@Log
public class PromiseInJava {

    private static final int POOL_SIZE = 20;
    private static final int TASK_NUM = 10;

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService singlePool = Executors.newSingleThreadExecutor();
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < TASK_NUM; i++) {
            final var id = i;
            CompletableFuture.supplyAsync(() -> hello(id));
        }

        // Just block for demo
        Thread.sleep(100_000);
    }

    private static boolean hello(int id) {
        try {
            System.out.println("Hi! I'm " + id);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return true;
    }
}
