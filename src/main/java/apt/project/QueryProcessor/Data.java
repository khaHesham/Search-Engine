package apt.project.QueryProcessor;

public class Data {   /* khaled : This class is basicaly for the retrieved data from the database (for simplicity) */
    public String word;
    public Integer[] locations ;
    public Double[] TF ;
    public Integer[][] frequancies;

    public Data(String word, Integer[] locations, Double[] tF, Integer[][] frequancies) {
        this.word = word;
        this.locations = locations;
        TF = tF;
        this.frequancies = frequancies;
    }


    public Integer[] indecies(int loc)
    {
        return frequancies[loc];
    }

   
}



