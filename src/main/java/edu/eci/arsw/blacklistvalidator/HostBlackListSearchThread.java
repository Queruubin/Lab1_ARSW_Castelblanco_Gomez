package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;
import java.util.List;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class HostBlackListSearchThread extends Thread {

    private int startRange;
    private int endRange;
    private String ipaddress;
    private HostBlacklistsDataSourceFacade skds;
    
    private int occurrencesCount = 0;
    private List<Integer> blackListOccurrences;
    private int checkedListsCount = 0;

    public HostBlackListSearchThread(int startRange, int endRange, String ipaddress) {
        this.startRange = startRange;
        this.endRange = endRange;
        this.ipaddress = ipaddress;
        this.skds = HostBlacklistsDataSourceFacade.getInstance();
        this.blackListOccurrences = new LinkedList<>();
    }

    @Override
    public void run() {
        for (int i = startRange; i < endRange; i++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)) {
                blackListOccurrences.add(i);
                occurrencesCount++;
            }
        }
    }

    public int getOccurrencesCount() {
        return occurrencesCount;
    }

    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }

    public int getCheckedListsCount() {
        return checkedListsCount;
    }
}