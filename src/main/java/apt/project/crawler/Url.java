package apt.project.crawler;


/*
 * simple wrapper class for url
 * */
public class Url implements Comparable<Url>{
    final String url;
    Double rank;

    public Url(String url) {
        this.url = url;
        rank = 0.0;
    }

    public Url(String url, Double rank) {
        this.url = url;
        this.rank = rank;
    }

    public String toString() {
        return url;
    }

    public Double getRank() {
        return rank;
    }

    public void updateRank(Double rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(Url url) {
        return rank.compareTo(url.rank);
    }
}
