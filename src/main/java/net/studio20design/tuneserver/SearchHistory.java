package net.studio20design.tuneserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: amir
 * Date: May 27, 2009
 * Time: 10:25:59 PM
 */
public class SearchHistory {
    List<String> history = new ArrayList<String>();
    List<Song> lastResults = Collections.emptyList();


    public SearchHistory() {
    }

    public void addToHistory(String q) {
        if (!history.contains(q)) {
            if (history.size() > 10) {
                history = history.subList(0, 9);
            }
            history.add(0, q);
        }
    }

    public void setLastResults(List<Song> lastResults) {
        this.lastResults = lastResults;
    }

    public List<String> getHistory() {
        return history;
    }

    public List<Song> getLastResults() {
        return lastResults;
    }
}
