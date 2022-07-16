package apt.project.crawler;

/*
 * simple threadsafe wrapper class that maintains a map of already crawled urls along with their fingerprints
 * */

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class CrawledUrls {
    private final Map<String, BitSet> crawledUrls;

    public CrawledUrls() {
        crawledUrls = new HashMap<>();
    }

    public CrawledUrls(Map<String, BitSet> crawledUrls) {
        this.crawledUrls = crawledUrls;
    }

    public synchronized void put(String url, BitSet fingerPrint){
        crawledUrls.put(url, fingerPrint);
    }

    public synchronized int size(){
        return crawledUrls.size();
    }

    public synchronized boolean HasUrl(String url) {
        return crawledUrls.containsKey(url);
    }

    public Map<String, BitSet> infrastructure(){
        return crawledUrls;
    }
}
