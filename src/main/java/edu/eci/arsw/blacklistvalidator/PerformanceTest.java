package edu.eci.arsw.blacklistvalidator;

import java.util.List;
import java.util.Scanner;

/**
 * Performance evaluation for Part III.
 * Tests IP 202.24.34.55 (dispersed) with different thread configurations.
 *
 * Configurations tested:
 *   1. 1 thread (sequential baseline)
 *   2. N threads (number of available cores)
 *   3. 2N threads (double the cores)
 *   4. 50 threads
 *   5. 100 threads
 *
 * HOW TO USE WITH jVisualVM:
 *   1. Open jVisualVM BEFORE running this class.
 *   2. In jVisualVM, go to the "Monitor" tab and the "Threads" tab.
 *   3. This program pauses between each test so you can observe and take screenshots.
 *   4. Press ENTER to continue to the next test after reviewing the graphs.
 */
public class PerformanceTest {

    private static final String TEST_IP = "202.24.34.55";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("============================================");
        System.out.println("  PARTE III — Evaluación de Desempeño");
        System.out.println("============================================");
        System.out.println("Máquina:        " + cores + " núcleos disponibles");
        System.out.println("IP de prueba:   " + TEST_IP + " (dispersa)");
        System.out.println("============================================");
        System.out.println("ANTES de continuar: asegurate de tener");
        System.out.println("jVisualVM abierto en la pestaña 'Monitor'");
        System.out.println("y 'Threads' para esta aplicación Java.");
        System.out.println("============================================\n");

        pause("Presiona ENTER para comenzar la prueba...");

        int[][] configs = {
            {1,      0},  // 1 hilo
            {cores,  0},  // N hilos (igual a núcleos)
            {cores * 2, 0},  // 2N hilos
            {50,     0},  // 50 hilos
            {100,    0},  // 100 hilos
        };

        String[] labels = {
            "1 hilo (secuencial, línea base)",
            cores + " hilos (igual a núcleos disponibles)",
            (cores * 2) + " hilos (doble de núcleos)",
            "50 hilos",
            "100 hilos",
        };

        for (int i = 0; i < configs.length; i++) {
            int nThreads = configs[i][0];
            String label = labels[i];

            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│ Prueba " + (i + 1) + "/5: " + label);
            System.out.println("│ OBSERVÁ jVisualVM → pestaña Monitor (CPU + Memoria)");
            System.out.println("│ y pestaña Threads (hilos activos). Tomá captura.");
            System.out.println("└─────────────────────────────────────────┘");

            pause("  Presiona ENTER para ejecutar esta prueba...");

            HostBlackListsValidator validator = new HostBlackListsValidator();

            long start = System.nanoTime();
            List<Integer> result = validator.checkHost(TEST_IP, nThreads);
            long end = System.nanoTime();

            double timeMs = (end - start) / 1_000_000.0;

            System.out.println("  >>> RESULTADO: " + String.format("%.2f ms", timeMs)
                + " | ocurrencias encontradas: " + result.size());
            System.out.println("  >>> REVISÁ jVisualVM ahora (post-prueba) y anotá:");
            System.out.println("      - Pico de CPU durante la prueba");
            System.out.println("      - Memoria Heap usada");
            System.out.println("      - Cuántos hilos se crearon (pestaña Threads)");

            if (i < configs.length - 1) {
                pause("\n  Presiona ENTER para ir a la siguiente prueba...");
            }
        }

        System.out.println("\n============================================");
        System.out.println("  ¡Pruebas completadas!");
        System.out.println("  Con los datos recolectados, hacé la gráfica");
        System.out.println("  de tiempo vs. número de hilos.");
        System.out.println("============================================");
        scanner.close();
    }

    private static void pause(String message) {
        System.out.print(message);
        scanner.nextLine();
    }
}
