
public class Data {   /* khaled : This class is basicaly for the retrieved data from the database (for simplicity) */
    public String word;
    public int[] locations ;
    public Double[] TF ;
    public Data(String word, int[] locations, Double[] tF) {
        this.word = word;
        this.locations = locations;
        TF = tF;
    }
   
}



