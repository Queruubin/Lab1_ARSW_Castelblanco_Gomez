package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.List;

import edu.eci.arsw.threads.punto1.CountThread1;

/**
 * Main class for Part I - Threads introduction.
 * 
 * Demonstrates two approaches:
 *   Section 1 — Without JOIN (partner's code using CountThread).
 *   Section 2 — With JOIN (using CountThread1), collecting results after all threads finish.
 * 
 * @authors Samuel Castelblanco, Ángela Gómez
 */
public class CountThreadsMain {

    public static void main(String a[]) throws InterruptedException {

        System.out.println("============================================");
        System.out.println("  SECCIÓN 1 — Sin JOIN (CountThread)");
        System.out.println("  Los hilos arrancan pero no se espera a que terminen.");
        System.out.println("  El run() no está sobreescrito, así que la impresión");
        System.out.println("  ocurre secuencialmente en el hilo principal.");
        System.out.println("============================================\n");

        CountThread.main(null);

        System.out.println("\n============================================");
        System.out.println("  SECCIÓN 2 — Con JOIN (CountThread1)");
        System.out.println("  Cada hilo guarda resultados en una lista.");
        System.out.println("  join() garantiza recogerlos todos al final.");
        System.out.println("============================================\n");

        int A = 0;
        int B = 1000;
        int numeroHilos = 3;

        CountThread1[] hilos = new CountThread1[numeroHilos];
        int cantidad = B - A + 1;
        int tamanoParte = cantidad / numeroHilos;
        int inicio = A;

        // Crear y lanzar los N hilos
        for (int i = 0; i < numeroHilos; i++) {
            int fin = (i == numeroHilos - 1) ? B : inicio + tamanoParte - 1;
            hilos[i] = new CountThread1("Hilo-" + i, inicio, fin + 1);
            hilos[i].start();
            inicio = fin + 1;
        }

        // Esperar a que todos terminen (JOIN)
        for (CountThread1 hilo : hilos) {
            hilo.join();
        }

        // Consolidar resultados
        List<Integer> resultado = new ArrayList<>();
        for (CountThread1 hilo : hilos) {
            resultado.addAll(hilo.devolverArr());
        }

        System.out.println("Total de números recolectados: " + resultado.size());
        System.out.println("Primeros 10: " + resultado.subList(0, Math.min(10, resultado.size())));
        System.out.println("Últimos 10:  " + resultado.subList(Math.max(0, resultado.size() - 10), resultado.size()));
    }
}
