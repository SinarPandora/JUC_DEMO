package com.lunch.learn.example.tools;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import lombok.extern.java.Log;

/**
 * Author: sinar
 * 2020/12/2 22:41
 */
@Log
public class SemaphoreExample {

    private static final Random RANDOM = new SecureRandom();
    // 25 file want to upload
    private static final int TASK_NUMS = 25;
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(TASK_NUMS);
    // A bit larger than uploader threads because file need prepare before upload
    private static final int POOL_SIZE = 15;

    public static void main(String[] args) throws InterruptedException {
        final S3Uploader uploader = new S3Uploader();
        final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < TASK_NUMS; i++) {
            final int id = i;
            pool.submit(() -> prepareAndUploadFile(id, uploader));
        }

        COUNT_DOWN_LATCH.await();
        pool.shutdownNow();
        log.info("UPLOAD SUCCESS");
    }

    private static boolean prepareAndUploadFile(int id, S3Uploader uploader) throws InterruptedException {
        final int data = prepareFile(id);
        uploader.upload(data);
        COUNT_DOWN_LATCH.countDown();
        return true;
    }

    private static int prepareFile(int id) throws InterruptedException {
        log.info("Prepare file " + id);
        Thread.sleep(RANDOM.nextInt(2) * 1000);
        return id; // processed data
    }

    // 5 threads uploader
    static class S3Uploader {
        private final Semaphore SEMAPHORE = new Semaphore(5);

        public void upload(int i) {
            try {
                SEMAPHORE.acquire();
                Thread.sleep(RANDOM.nextInt(4) * 1000);
                log.info("Upload " + i + " done!");
                SEMAPHORE.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
