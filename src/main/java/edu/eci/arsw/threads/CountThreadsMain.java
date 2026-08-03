package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Demonstrates the effect of join():
 *   Section 1 - WITHOUT join(): results are read before threads are
 *               guaranteed to finish, so the collected data can be
 *               incomplete or inconsistent between runs.
 *   Section 2 - WITH join(): the main thread waits for every worker
 *               thread to finish, guaranteeing complete, consistent results.
 *
 * @author Samuel Castelblanco, Ángela Gómez
 */
public class CountThreadsMain {

    public static void main(String[] args) throws InterruptedException {

        int rangeStart = 0;
        int rangeEnd = 1000;
        int numberOfThreads = 3;

        System.out.println("============================================");
        System.out.println("  SECTION 1 - WITHOUT join()");
        System.out.println("  Threads are started, but the main thread does NOT wait for them.");
        System.out.println("  Results are read immediately, before completion is guaranteed.");
        System.out.println("============================================\n");

        List<Integer> resultWithoutJoin = runThreadsWithoutJoin(rangeStart, rangeEnd, numberOfThreads);

        System.out.println("Total numbers collected: " + resultWithoutJoin.size());
        System.out.println("Expected total if every thread had finished: " + (rangeEnd - rangeStart + 1));
        System.out.println("This count is unreliable: it can vary between runs, and may even match the expected");

        System.out.println("============================================");
        System.out.println("  SECTION 2 - WITH join()");
        System.out.println("  join() guarantees the results are collected correctly and consistently.");
        System.out.println("============================================\n");

        List<Integer> resultWithJoin = runThreadsWithJoin(rangeStart, rangeEnd, numberOfThreads);

        System.out.println("Total numbers collected: " + resultWithJoin.size());
        System.out.println("First 10: " + resultWithJoin.subList(0, Math.min(10, resultWithJoin.size())));
        System.out.println("Last 10:  " + resultWithJoin.subList(Math.max(0, resultWithJoin.size() - 10), resultWithJoin.size()));
    }

    /**
     * Starts N threads but does NOT wait for them to finish before
     * reading their results. This can produce incomplete or
     * inconsistent data.
     */
    private static List<Integer> runThreadsWithoutJoin(int rangeStart, int rangeEnd, int numberOfThreads) {
        CountThread1[] threads = createThreads(rangeStart, rangeEnd, numberOfThreads);

        for (CountThread1 thread : threads) {
            thread.start();
        }

        // No join() here: results are read right away, without waiting.
        List<Integer> result = new ArrayList<>();
        for (CountThread1 thread : threads) {
            result.addAll(thread.getNumbers());
        }
        return result;
    }

    /**
     * Starts N threads and waits for all of them to finish using
     * join() before reading their results. This guarantees the final
     * list is always complete and consistent.
     */
    private static List<Integer> runThreadsWithJoin(int rangeStart, int rangeEnd, int numberOfThreads) throws InterruptedException {
        CountThread1[] threads = createThreads(rangeStart, rangeEnd, numberOfThreads);

        for (CountThread1 thread : threads) {
            thread.start();
        }

        // join() blocks the main thread until each worker thread finishes.
        for (CountThread1 thread : threads) {
            thread.join();
        }

        List<Integer> result = new ArrayList<>();
        for (CountThread1 thread : threads) {
            result.addAll(thread.getNumbers());
        }
        return result;
    }

    private static CountThread1[] createThreads(int rangeStart, int rangeEnd, int numberOfThreads) {
        CountThread1[] threads = new CountThread1[numberOfThreads];
        int totalCount = rangeEnd - rangeStart + 1;
        int chunkSize = totalCount / numberOfThreads;
        int start = rangeStart;

        for (int i = 0; i < numberOfThreads; i++) {
            int end = (i == numberOfThreads - 1) ? rangeEnd : start + chunkSize - 1;
            threads[i] = new CountThread1("Thread-" + i, start, end + 1);
            start = end + 1;
        }
        return threads;
    }
}