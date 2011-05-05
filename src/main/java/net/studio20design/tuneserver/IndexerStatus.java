package net.studio20design.tuneserver;

import java.util.ArrayList;
import java.util.List;

/**
 * User: amir
 * Date: May 22, 2009
 * Time: 11:14:10 PM
 */
public class IndexerStatus {
    private IndexerStatus() {

    }

    final private static IndexerStatus instance = new IndexerStatus();

    final private List<Song> songs = new ArrayList<Song>();
    private boolean indexing;
    private int totalToIndex;
    private boolean waitingToIndex;

    public static IndexerStatus getInstance() {
        return instance;
    }

    public boolean isIndexing() {
        return indexing;
    }

    public void setIndexing(boolean indexing) {
        this.indexing = indexing;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public int getTotalToIndex() {
        return totalToIndex;
    }

    public void setTotalToIndex(int totalToIndex) {
        this.totalToIndex = totalToIndex;
    }

    public int getTotalIndexed() {
        return songs.size();
    }

    public int getPercentComlete() {
        return getTotalToIndex() > 0 ? 100 * getTotalIndexed() / getTotalToIndex() : 0;
    }

    public boolean isWaitingToIndex() {
        return waitingToIndex;
    }

    public void setWaitingToIndex(boolean waitingToIndex) {
        this.waitingToIndex = waitingToIndex;
    }

    public void complete() {
        setIndexing(false);
        setWaitingToIndex(false);
    }
}
