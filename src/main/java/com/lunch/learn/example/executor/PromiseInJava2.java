package com.lunch.learn.example.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.java.Log;

@Log
public class PromiseInJava2 {

    private static final int TASK_NUM = 100;
    private static final int POOL_SIZE = 4;

    public static void main(String[] args) {
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        final List<Future<List<Integer>>> results = new ArrayList<>();
        for (int i = 0; i < TASK_NUM; i++) {
            int finalI = i;
            results.add(pool.submit(() -> createList(finalI)));
        }
        int sum = results.stream().flatMap(fu -> {
            try {
                return fu.get().stream();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).reduce(0, Integer::sum);
        log.info("Result is" + sum);
        pool.shutdown();
    }

    public static List<Integer> createList(int idx) {
        log.info("START + " + idx);
        return IntStream.rangeClosed(1, 4).boxed().collect(Collectors.toList());
    }
}
