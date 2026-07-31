package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT = 5;
    
    /**
     * Check the given host's IP address in all the available black lists using N threads,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * @param ipaddress suspicious host's IP address.
     * @param N number of threads to use in the search.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N) {
        
        LinkedList<Integer> blackListOccurrences = new LinkedList<>();
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        
        int totalServers = skds.getRegisteredServersCount();
        
        // Calcular el tamaño de cada segmento y el residuo (para manejar N par o impar)
        int segmentSize = totalServers / N;
        int remainder = totalServers % N;
        
        HostBlackListSearchThread[] threads = new HostBlackListSearchThread[N];
        
        int startRange = 0;
        
        // Crear y lanzar los hilos distribuyendo el espacio de búsqueda
        for (int i = 0; i < N; i++) {
            int endRange = startRange + segmentSize;
            // Si es el último hilo, le sumamos el residuo para cubrir la totalidad de los servidores
            if (i == N - 1) {
                endRange += remainder;
            }
            
            threads[i] = new HostBlackListSearchThread(startRange, endRange, ipaddress);
            threads[i].start();
            
            startRange = endRange;
        }
        
        // Esperar a que todos los hilos terminen su ejecución (join)
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "Thread interrupted", ex);
            }
        }
        
        int totalOccurrences = 0;
        int checkedListsCount = 0;
        
        // Consolidar resultados de todos los hilos
        for (int i = 0; i < N; i++) {
            totalOccurrences += threads[i].getOccurrencesCount();
            checkedListsCount += threads[i].getCheckedListsCount();
            blackListOccurrences.addAll(threads[i].getBlackListOccurrences());
        }
        
        // Reportar según el umbral de alarma
        if (totalOccurrences >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }               
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, totalServers});
        
        return blackListOccurrences;
    }
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
}