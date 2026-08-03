package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to practice the use of threads.
 * Each thread collects a subrange of numbers into its own list.
 * @author Samuel 
 */
public class CountThread1 extends Thread {

    private int start;
    private int end;
    private String threadName;
    private List<Integer> numbers;

    public CountThread1(String threadName, int start, int end) {
        this.threadName = threadName;
        this.start = start;
        this.end = end;
        this.numbers = new ArrayList<>();
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public String getThreadName() {
        return threadName;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            numbers.add(i);
        }
    }
}