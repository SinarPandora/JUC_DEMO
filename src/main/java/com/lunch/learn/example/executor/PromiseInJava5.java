package com.lunch.learn.example.executor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import io.vavr.control.Either;
import lombok.extern.java.Log;

@Log
public class PromiseInJava5 {

    private static final int TASK_NUM = 100;
    private static final int POOL_SIZE = 4;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        final ExecutorCompletionService<List<Integer>> ecs = new ExecutorCompletionService<>(pool);
        for (int i = 0; i < TASK_NUM; i++) {
            final int finalI = i;
            ecs.submit(() -> createList(finalI));
        }
        // Use either to store errs
        Stream<Either<Exception, List<Integer>>> resultStream = IntStream.range(0, TASK_NUM).mapToObj(ignored -> {
            try {
                return Either.right(ecs.take().get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                return Either.left(e);
            }
        });
        List<Either<Exception, List<Integer>>> results = resultStream.collect(Collectors.toList());
        pool.shutdown();
        Integer sum = results.stream()
                // empty as default
                .map(either -> either.getOrElse(Collections.emptyList()))
                .flatMap(List::stream)
                .reduce(0, Integer::sum);
        log.info("Result is" + sum);
    }

    public static List<Integer> createList(int idx) {
        log.info("START + " + idx);
        return IntStream.rangeClosed(1, 4).boxed().collect(Collectors.toList());
    }
}
