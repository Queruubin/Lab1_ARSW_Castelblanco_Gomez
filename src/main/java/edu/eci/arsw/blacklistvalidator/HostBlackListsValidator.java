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
        
        // Managing the segment size, depending if the N value is even or odd. 
        int segmentSize = totalServers / N;
        int remainder = totalServers % N;
        
        HostBlackListSearchThread[] threads = new HostBlackListSearchThread[N];
        
        int startRange = 0;
        
        // Creating threads depending on the searching space 
        for (int i = 0; i < N; i++) {
            int endRange = startRange + segmentSize;
            //  If its the last thread, we add the residue in order to cover up all the services 
            if (i == N - 1) {
                endRange += remainder;
            }
            
            threads[i] = new HostBlackListSearchThread(startRange, endRange, ipaddress);
            threads[i].start();
            
            startRange = endRange;
        }
        
        // EWaits until all threads finish their execution 
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "Thread interrupted", ex);
            }
        }
        
        int totalOccurrences = 0;
        int checkedListsCount = 0;
        
        // Reunite the thread results 
        for (int i = 0; i < N; i++) {
            totalOccurrences += threads[i].getOccurrencesCount();
            checkedListsCount += threads[i].getCheckedListsCount();
            blackListOccurrences.addAll(threads[i].getBlackListOccurrences());
        }
        
        // Report within the warning threshold 
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