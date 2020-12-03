package com.lunch.learn.example.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.java.Log;
/**
 * Author: sinar
 * 2020/12/2 23:38
 */
@Log
public class CreateExecutor {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // TODO! DO WHAT YOU WANT
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            results.add(pool.submit(() -> {
                // System.out.println(finalI + " times HELLO YCY!");
                return finalI + " times HELLO YCY!";
            }));
        }
        System.out.println("HELLO NATE!");
        for (Future<String> result : results) {
            System.out.println(result.get());
        }
        Thread.sleep(10000);
    }
}
