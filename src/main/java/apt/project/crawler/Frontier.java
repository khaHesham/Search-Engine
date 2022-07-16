package apt.project.crawler;

import apt.project.crawler.Url;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class Frontier {

    private Queue<Url> frontier;
    private final Graph<String, DefaultEdge> WebGraph;
    private Map<String, Double> urlRank;

    public Frontier(Graph<String, DefaultEdge> WebGraph, List<String> seeds){
        frontier = new PriorityQueue<>();
        urlRank = new HashMap<>();
        this.WebGraph = WebGraph;

        for(String seed: seeds)
            frontier.add(new Url(seed));
    }
    public Frontier(Graph<String, DefaultEdge> WebGraph, Queue<Url> frontier){
        urlRank = new HashMap<>();
        this.WebGraph = WebGraph;
        this.frontier = frontier;
    }

    public synchronized boolean HasUrl(String url) {
        return WebGraph.containsVertex(url);
    }

    public synchronized String FetchUrl(){
        while(frontier.isEmpty())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                return "";
            }
        }
        return frontier.poll().toString();
    }

    /*
    * Insert an url fetched from a webpage into the frontier and updates the WebGraph
    * First it checks whether the url is a new one, if not it's ignored
    * An edge is added from the current url to the new url
    * */
    public synchronized void InsertUrl(String current_url, String url) {
        boolean newVertex = WebGraph.addVertex(url);
        if(newVertex)
        {
            frontier.add(new Url(url));
            notifyAll();
        }
        WebGraph.addEdge(current_url, url);
    }

    /*
    * Compute the PageRank of the WebGraph and updates the frontier queue accordingly
    * The update is done by dequeue urls from frontier, update their ranks
    * */
    public synchronized void Update(){

        //Compute the new PageRank probability distribution
        urlRank = new PageRank<>(WebGraph).getScores();

        //Updates the frontier
        Queue<Url> newFrontier = new PriorityQueue<>();
        while (!frontier.isEmpty())
        {
            Url url = frontier.poll();
            double rank = urlRank.get(url.toString());
            url.updateRank(rank);
            newFrontier.add(url);
        }
        frontier = newFrontier;
    }

    public Map<String, Double> getUrlRank(){
        return urlRank;
    }

    public Queue<Url> infrastructure(){
        return frontier;
    }
}