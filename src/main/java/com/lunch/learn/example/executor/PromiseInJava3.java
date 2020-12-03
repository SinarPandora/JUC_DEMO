package com.lunch.learn.example.executor;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.java.Log;

@Log
public class PromiseInJava3 {

    private static final int TASK_NUM = 100;
    private static final int POOL_SIZE = 4;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        final ExecutorCompletionService<List<Integer>> ecs = new ExecutorCompletionService<>(pool);
        for (int i = 0; i < TASK_NUM; i++) {
            int finalI = i;
            ecs.submit(() -> createList(finalI));
        }
        int sum = IntStream.range(0, TASK_NUM).mapToObj(ignored -> {
            try {
                return ecs.take().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).flatMap(List::stream).reduce(0, Integer::sum);
        log.info("Result is" + sum);
        pool.shutdown();
    }

    public static List<Integer> createList(int idx) {
        log.info("START + " + idx);
        return IntStream.rangeClosed(1, 4).boxed().collect(Collectors.toList());
    }
}
