/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 * PART ONE 
 * 1. 
 */
public class CountThread extends Thread {

    private int numberA;
    private int numberB;
    private String nameThread; 

    private CountThread (String nameThread, int numberA, int numberB) {
        this.nameThread = nameThread;
        this.numberA = numberA;
        this.numberB = numberB; 
    }

    public void printingRange(int numberA, int numberB) {
        for (int i = numberA; i <= numberB; i++) {
            System.out.println(i); 
        }
    }

    private void areNumbersValid (int numberA, int numberB) {
        int difference = numberB - numberA; 
        if (difference < 0) {
            throw new IllegalArgumentException ("Error: number A is greater than number B.");
        }

    }

    /**
     * Runs the 3 threads that prints the elements given a range into 3 subranges.  
     */
    public static void main(String[] args) {
        int a = 1;
        int b = 10;

    
    CountThread thread1 = new CountThread("Thread1",a,b/3);
    CountThread thread2 = new CountThread("Thread2",b/3+1,b*2/3);
    CountThread thread3 = new CountThread("Thread3",(b*2/3)+1,b);
    
    thread1.areNumbersValid(a,b/3);
    thread1.printingRange(a,b/3);
    thread1.start();

    thread2.areNumbersValid(b/3+1,b*2/3);
    thread2.printingRange(b/3+1,b*2/3);
    thread2.start();

    thread3.areNumbersValid((b*2/3)+1,b);
    thread3.printingRange((b*2/3)+1,b);
    thread3.start();
    

    }
    

}
