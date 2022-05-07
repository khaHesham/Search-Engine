
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import org.apache.lucene.util.fst.PairOutputs.Pair;

import MongoDB.URLs_DB;
import MongoDB.InvertedFiles_DB;

public class QueryProcessor {

    Parser_ parser;
    String Query; // normal string for now

    public QueryProcessor(String Q) {
        this.parser = new Parser_("");
        this.Query = Q;
    }

    public String[] processInputQuery() throws IOException {
        String[] query = Query.split(" ");

        if (query.length != 0) {
            query = parser.processData(query);
        }
        return query;
    }

    public List<Entry<Integer, Double>> Ranking() throws IOException {
        String[] indecies = null;
        String[] processed_input = processInputQuery();
        Gson gson = new Gson();

        List<Map<Integer, Double>> TF_IDF = new ArrayList<Map<Integer, Double>>();

        // final String invertedfiles_host =
        // "mongodb+srv://searchengine:mazaritaengine111222333@cluster0.pah6z.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
        InvertedFiles_DB invertedfiles_db = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "InvertedFiles");

        Map<Integer, Double> map_ = new HashMap<Integer, Double>();
        for (String st : processed_input) {

            Data data = gson.fromJson(invertedfiles_db.RetrieveData(st), Data.class);
            if (data!= null)
                for (int i = 0; i < data.locations.length; i++) {
                    Double tf_idf = 0.0;
                    tf_idf = data.TF[i] * (5000.0 / Double.valueOf(data.locations.length)); // tf*idf of each word in                                                                                           // each document
                    if (map_.containsKey(data.locations[i])) {
                        Double temp = map_.get(data.locations[i]);
                        temp += tf_idf; // sum all the tf*idf values of the same location
                        map_.put(data.locations[i], temp);
                    } else
                        map_.put(data.locations[i], tf_idf);
                }

        }

        List<Entry<Integer, Double>> list = new ArrayList<>(map_.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        list.forEach(System.out::println);

        return list; // sort the map then return the Descending sorted list
    }

    public static void main(String[] args) throws IOException {
        QueryProcessor q = new QueryProcessor("Temperature is Amazing Today and every day in egypt");
        q.Ranking();

    }

}
