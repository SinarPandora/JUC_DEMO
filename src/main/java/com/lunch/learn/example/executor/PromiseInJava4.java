package com.lunch.learn.example.executor;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import lombok.extern.java.Log;

@Log
// JAVA 11 Only
public class PromiseInJava4 {

    private static final int TASK_NUM = 100;
    private static final int POOL_SIZE = 4;

    public static void main(String[] args) throws Exception {
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        final ExecutorCompletionService<Boolean> ecs = new ExecutorCompletionService<>(pool);
        ecs.submit(() -> createReport(1));
        ecs.submit(() -> createReportErr(2));
        for (int i = 0; i < TASK_NUM - 2; i++) {
            int finalI = i + 2;
            ecs.submit(() -> createReport(finalI));
        }
        // Use either to store errs
        Optional<Exception> errorOpt = IntStream.range(0, TASK_NUM).mapToObj(ignored -> {
            try {
                ecs.take().get();
                return Optional.<Exception>empty();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                return Optional.<Exception>of(e);
            }
        }).flatMap(Optional::stream).findAny();

        if (errorOpt.isPresent()) {
            log.warning("FAIL TO GENERATE REPORT");
            pool.shutdownNow();
            // throw errorOpt.get();
        } else {
            log.warning("FINISHED!");
            pool.shutdown();
        }

    }

    public static boolean createReportErr(int idx) throws InterruptedException {
        log.info("START + " + idx);
        Thread.sleep(3000);
        throw new RuntimeException("ERROR!");
    }

    public static boolean createReport(int idx) throws InterruptedException {
        log.info("START + " + idx);
        Thread.sleep(10000);
        log.info("START + " + idx);
        return true;
    }
}
