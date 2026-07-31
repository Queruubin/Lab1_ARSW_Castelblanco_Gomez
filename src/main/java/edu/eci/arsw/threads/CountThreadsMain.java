/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.List;

import edu.eci.arsw.threads.punto1.CountThread1;

/**
 *
 * @author samuelito
 */
public class CountThreadsMain {

    private int A = 0;
    private int B = 1000;
    
    public static void main(String a[])throws InterruptedException{

        // Primer punto








        // Segundo punto -------------------------------------------------------------------------

        int A = 0;  
        int B = 1000;
        int numeroHilos = 3;

        CountThread1[] hilos = new CountThread1[numeroHilos];

        int cantidad = B - A + 1;
        int tamanoParte = cantidad / numeroHilos;

        int inicio = A;

        for (int i = 0; i < numeroHilos; i++) {

            int fin;

            if (i == numeroHilos - 1) {
                fin = B;
            } else {
                fin = inicio + tamanoParte - 1;
            }

            hilos[i] = new CountThread1("Hilo-" + i, inicio, fin + 1);
            hilos[i].start();

            inicio = fin + 1;
        }
        for (CountThread1 hilo : hilos) {
            hilo.join();
        }

        List<Integer> resultado = new ArrayList<>();

        for (CountThread1 hilo : hilos) {
            resultado.addAll(hilo.devolverArr());
        }

        for (Integer i: resultado) {
            System.out.println(i);
            
        }
    
    }
}