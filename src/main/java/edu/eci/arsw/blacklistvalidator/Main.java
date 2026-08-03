package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 * Main test program for Black List Search.
 * Tests both IPs mentioned in the lab statement:
 *   - 202.24.34.55 : reported in blacklists (dispersed)
 *   - 212.24.24.55 : NOT in any blacklist
 *
 * @author hcadavid
 */
public class Main {

    public static void main(String a[]) {
        HostBlackListsValidator hblv = new HostBlackListsValidator();

        // Número de hilos = núcleos disponibles
        int nThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Núcleos disponibles: " + nThreads);
        System.out.println("============================================\n");

        // ---- Caso 1: IP dispersa (reportada en listas negras) ----
        System.out.println("[Caso 1] IP dispersa: 202.24.34.55");
        testHost(hblv, "202.24.34.55", nThreads);

        // ---- Caso 2: IP limpia (NO está en ninguna lista negra) ----
        System.out.println("\n[Caso 2] IP limpia: 212.24.24.55");
        testHost(hblv, "212.24.24.55", nThreads);
    }

    private static void testHost(HostBlackListsValidator validator, String ip, int threads) {
        long start = System.currentTimeMillis();
        List<Integer> blackListOcurrences = validator.checkHost(ip, threads);
        long end = System.currentTimeMillis();

        System.out.println("  Tiempo: " + (end - start) + " ms");
        System.out.println("  Ocurrencias en listas negras: " + blackListOcurrences.size());
        if (!blackListOcurrences.isEmpty()) {
            System.out.println("  Listas negras donde se encontró: " + blackListOcurrences);
        }
    }
}
