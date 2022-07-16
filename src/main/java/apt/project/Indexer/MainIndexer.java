package apt.project.Indexer;

import apt.project.MongoDB.DB;
import apt.project.crawler.Crawler;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class MainIndexer {
    static final int MAX_THREADS = 8;

    public static void main(String[] args) throws IOException {
        Gson gson=new Gson();
        pages page;
        Queue<String> urlQueue = new LinkedList<>();

        DB invertedfiles_db = new DB("localhost", 27017, "SearchEngine", "InvertedFiles");
        DB URLS_db = new DB("localhost", 27017, "SearchEngine", "URLS");

        for (int doc_index=0;doc_index<URLS_db.getNumberOfDocuments();doc_index++) {

            page = gson.fromJson(URLS_db.Retrievepages(doc_index), pages.class);  //Retrieve from the urls database
            urlQueue.add(page.link);
        }

        UrlQueue urls= new UrlQueue(urlQueue);

        Indexer[] indexerThreads = new Indexer[MAX_THREADS];

        for (int i = 0; i < MAX_THREADS; i++) {
            indexerThreads[i] = new Indexer(urls);
            indexerThreads[i].start();
        }
    }
}
