package apt.project.crawler;

import apt.project.MongoDB.DB;
import com.google.gson.Gson;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;




public class Main {

    static final int MAX_THREADS = 8;

    static Graph<String, DefaultEdge> webGraph;
    static FingerPrints fingerPrints;
    static CrawledUrls crawledUrls;
    static Frontier frontier;


    public static void main(String []args) throws IOException {

        // Creating pages, .robots, directories respectively.
        new File (Paths.get(System.getProperty("user.dir")).resolve("pages").resolve(".robots").toString()).mkdirs();

        Scanner scanner = null;
        int flag;
        try {
            scanner = new Scanner(new File("flag.txt"));
            flag = scanner.nextInt();
        } catch (FileNotFoundException e) {
            flag = 0;
        }


        // Either the file exists and the crawler terminated normally last time, or it doesn't and it's a new start.
        if (flag == 0) {

            List<String> seeds = new ArrayList<>();
            webGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
            fingerPrints = new FingerPrints();
            crawledUrls = new CrawledUrls();

            try {
                scanner = new Scanner(new File("seeds.txt"));
            } catch (FileNotFoundException e) {
                System.err.println("An error occurred reading seeds.txt file.");
                System.exit(1);
            }

            while (scanner.hasNext()) {
                String url = scanner.next();
                seeds.add(url);
                webGraph.addVertex(url);
            }

            frontier = new Frontier(webGraph, seeds);
        } else {
            System.err.println("Continuing....");
            Load();
        }
        Crawler [] crawlerThreads = new Crawler[MAX_THREADS];
        SignalHandler handler = signal -> {
            for (Crawler t : crawlerThreads) {
                t.Terminate();
                if (t.getState() == Thread.State.WAITING)
                    t.interrupt();
            }
        };

        Signal.handle(new Signal("INT"), handler);

        // Creating the crawler threads.
        for (int i = 0; i < MAX_THREADS; i++) {
            crawlerThreads[i] = new Crawler(frontier, fingerPrints, crawledUrls);
            crawlerThreads[i].setName(String.format("crawler-thread-%d", i));
            crawlerThreads[i].start();
        }

        for (Thread t : crawlerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("main thread interrupted");
            }
        }

        // Flush only if an interrupt happened.
        if (crawlerThreads[0].terminated)
            Flush();
        printCrawlerOutFile();
    }


    private static void printCrawlerOutFile() throws IOException {
        DB urls_db = new DB("localhost", 27017, "SearchEngine", "URLS");
        Gson gson=new Gson();
        Map<String, BitSet> crawledPages = crawledUrls.infrastructure();
        Map<String, Double> allRanks = frontier.getUrlRank();

        Map<String, Double> pageRanks = new HashMap<>();
        Double rankSum = 0.0;
        for(String url: crawledPages.keySet())
        {
            pageRanks.put(url, allRanks.get(url));
            rankSum += allRanks.get(url);
        }
        Double finalRankSum = rankSum;
        pageRanks.replaceAll((u, v) -> pageRanks.get(u) / finalRankSum);

            int index=0;
            for (Map.Entry<String, Double> entry: pageRanks.entrySet())
            {
                String url = entry.getKey();
                Document doc;
                try {
                    doc = Jsoup.connect("https://" + url).get();
                } catch (Exception exception)
                {
                    exception.printStackTrace();
                    continue;
                }

                String description = "";
                String title = "";

                if (doc.select("meta[name=description]").size() != 0)
                     description = doc.select("meta[name=description]").get(0).attr("content");

                // String keywords = doc.select("meta[name=keywords]").first().attr("content");

                Double rank = entry.getValue();
                if (doc.getElementsByTag("title").size() != 0)
                    title = doc.getElementsByTag("title").get(0).text();

                urls_db.insert_pages(index,"https://" + url,description,title,rank);
                index++;
            }
    }

    /**
     * Flushes Data structures in disk in case of interruption
     */
    private static void Flush(){
        //Add a flag, so we can know where to start the new crawl
        try {
            FileWriter flag = new FileWriter("flag.txt");
            flag.write("1");

            flag.close();
        } catch (IOException e) {
            System.out.println("An error occurred in flag file");
        }


        Map<String, BitSet> crawledPages = crawledUrls.infrastructure();
        Queue<Url> queue = frontier.infrastructure();
        HashSet<BitSet> fingerprints = fingerPrints.infrastructure();

        //Crawled urls
        try {
            FileWriter CU = new FileWriter("crawledUrls.txt");
            CU.write(crawledPages.size() + "\n");

            for (Map.Entry<String, BitSet> entry: crawledPages.entrySet())
                CU.write(entry.getKey() + " " + FingerPrints.toString(entry.getValue()) + "\n");

            CU.close();
        } catch (IOException e) {
            System.out.println("An error occurred in printing crawledUrls file");
        }

        //frontier
        try {
            FileWriter FR = new FileWriter("frontier.txt");

            FR.write(queue.size() + "\n");
            for (Url url: queue)
                FR.write(url.toString() + " " + url.getRank() + "\n");

            FR.close();
        } catch (IOException e) {
            System.out.println("An error occurred in printing frontier file");
        }

        //fingerPrints
        try {
            FileWriter FP = new FileWriter("fingerPrints.txt");

            for (BitSet fingerPrint: fingerprints)
                FP.write(FingerPrints.toString(fingerPrint) + "\n");

            FP.close();
        } catch (IOException e) {
            System.out.println("An error occurred in printing fingerPrints file");
        }

        //WebGraph
        try {
            FileWriter WG = new FileWriter("webGraph.txt");

            //writing vertices
            WG.write(webGraph.vertexSet().size() + "\n");
            for (String v : webGraph.vertexSet())
                WG.write(v + "\n");

            //writing edges
            WG.write(webGraph.edgeSet().size() + "\n");
            for (DefaultEdge e : webGraph.edgeSet())
                WG.write(e + "\n");

            WG.close();
        } catch (IOException e) {
            System.out.println("An error occurred in printing webGraph file");
        }
    }

    /**
     * Load loads the urls, and their associated data from existing files, created last time the crawler was interrupted.
     * */
    private static void Load() {

        Scanner fingerPrintsScanner = null;
        Scanner webGraphScanner = null;
        Scanner frontierScanner = null;
        Scanner crawledUrlsScanner = null;

        try {
            fingerPrintsScanner = new Scanner(new File("fingerPrints.txt"));
            webGraphScanner = new Scanner(new File("webGraph.txt"));
            frontierScanner = new Scanner(new File("frontier.txt"));
            crawledUrlsScanner = new Scanner(new File("crawledUrls.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("error happened opened one of the files: fingerPrints.txt, webGraph.txt, frontier.txt, crawledUrls.txt");
            System.exit(1);
        }


        // Load the fingerprints
        HashSet<BitSet> newFingerPrints = new HashSet<>();
        while(fingerPrintsScanner.hasNext())
        {
            String binary = fingerPrintsScanner.next();
            newFingerPrints.add(FingerPrints.toBitSet(binary));
        }

        fingerPrints = new FingerPrints(newFingerPrints);

        // Create the webGraph
        webGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

        // Read the vertices
        int vNum  = webGraphScanner.nextInt();
        for (int i = 0; i < vNum; i++) {
            String vertex = webGraphScanner.next();
            webGraph.addVertex(vertex);
        }
        int eNum  = webGraphScanner.nextInt();

        // Read the edges in the form (S : D)
        for (int i = 0; i < eNum; i++) {
            String source = webGraphScanner.next().substring(1);
            webGraphScanner.next();//discard the dummy :
            String temp = webGraphScanner.next();
            String destination = temp.substring(0, temp.length() - 1);
            webGraph.addEdge(source, destination);
        }

        // Load the frontier
        Queue<Url> newFrontier = new PriorityQueue<>();
        int frontierSize  = frontierScanner.nextInt();
        for (int i = 0; i < frontierSize; i++) {
            String urlString = frontierScanner.next();
            Double rank = frontierScanner.nextDouble();
            Url url = new Url(urlString, rank);
            newFrontier.add(url);
        }

        frontier = new Frontier(webGraph, newFrontier);

        // Load crawledUrls
        Map<String, BitSet> newCrawledUrls = new HashMap<>();
        int mapSize = crawledUrlsScanner.nextInt();
        for (int i = 0; i < mapSize; i++) {
            String url = crawledUrlsScanner.next();
            BitSet bitset = FingerPrints.toBitSet(crawledUrlsScanner.next());
            newCrawledUrls.put(url, bitset);
        }
        crawledUrls = new CrawledUrls(newCrawledUrls);

        fingerPrintsScanner.close();
        webGraphScanner.close();
        frontierScanner.close();
        crawledUrlsScanner.close();
    }
}