/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 * @author Samuel + Ángela 
 * PART ONE 
 * 1. 
 */
public class CountThread extends Thread {

    private int numberA;
    private int numberB;
    private String nameThread; 

    public CountThread (String nameThread, int numberA, int numberB) {
        this.nameThread = nameThread;
        this.numberA = numberA;
        this.numberB = numberB; 
    }

    @Override
    public void run() {
        areNumbersValid(numberA, numberB);
        printingRange(numberA, numberB);
    }

    public void printingRange(int numberA, int numberB) {
        for (int i = numberA; i <= numberB; i++) {
            // Shows what thread is printing what set of numbers
            System.out.println(nameThread + ": " + i);

        }
    }

    private void areNumbersValid (int numberA, int numberB) {
        int difference = numberB - numberA; 
        if (difference < 0) {
            throw new IllegalArgumentException ("Error: number A is greater than number B.");
        }

    }

    /**
     * Runs the 3 threads that prints the elements given a range into 3 subranges using the start and run methods. 
     */
    public static void main(String[] args) {
        int a = 1;
        int b = 100;

    CountThread thread1 = new CountThread("Thread1", a, b / 3);
    CountThread thread2 = new CountThread("Thread2", b / 3 + 1, b * 2 / 3);
    CountThread thread3 = new CountThread("Thread3", (b * 2 / 3) + 1, b);

    System.out.println("=== Using run() (sequential) ===");

    thread1.run();
    thread2.run();
    thread3.run();
    

    System.out.println("=== Using start() (concurrent) ===");
    CountThread thread1b = new CountThread("Thread1", a, b / 3);
    CountThread thread2b = new CountThread("Thread2", b / 3 + 1, b * 2 / 3);
    CountThread thread3b = new CountThread("Thread3", (b * 2 / 3) + 1, b);

    thread1b.start();
    thread2b.start();
    thread3b.start();

    }

}
