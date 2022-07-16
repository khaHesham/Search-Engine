package apt.project.Indexer;

import apt.project.crawler.Url;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class UrlQueue {
    private final Queue<String> urls;
    private int doc_index;

    public UrlQueue(Queue<String> urls){
        this.urls = urls;
        doc_index = 1;
    }

    public synchronized String FetchUrl(){
        doc_index++;
        return urls.poll();
    }

    public synchronized int getdoc_index(){
        return doc_index;
    }

    public synchronized boolean isEmpty()
    {
        return urls.isEmpty();
    }
}
