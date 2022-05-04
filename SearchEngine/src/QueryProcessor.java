
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import MongoDB.URLs_DB;
import MongoDB.InvertedFiles_DB;

public class QueryProcessor {

    Parser_ parser;
    String Query; // normal string for now

    public QueryProcessor(String Q) {
        parser = new Parser_("");
        Query = Q;
    }

    public String[] processInputQuery() throws IOException {
        String[] query = Query.split(" ");

        if (query.length != 0) {
            query = parser.processData(query);
        }
        return query;
    }

    public String[] Ranking() throws IOException {
        String[] indecies = null;
        String[] processed_input = processInputQuery();

        Double TF = 0.0;
        Double IDF = 0.0;
        List<Double> TF_IDF = new ArrayList<Double>();

        InvertedFiles_DB invertedfiles_db = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "InvertedFiles");
        for (String s : processed_input) {
            Map<String, String> data = invertedfiles_db.find(s);
            String index = data.get("locations");
            if (index != null) {
                indecies = index.split(" ");
                for (String s2 : indecies) {
                    System.out.println(s + " " + s2);



                }

            }
        }
        return processed_input;
    }

    public static void main(String[] args) throws IOException {
        QueryProcessor q = new QueryProcessor("temperature is amazing today");
        q.Ranking();

    }

}
