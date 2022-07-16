package apt.project.QueryProcessor;

import apt.project.Indexer.Parser_;
import apt.project.Indexer.pages;
import apt.project.MongoDB.DB;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class QueryProcessor {

    Parser_ parser;
    String Query; // normal string for now
    final double const1 = 0.4;
    final double const2 = 0.6;

    public QueryProcessor(String Q) {
        this.parser = new Parser_("");
        this.Query = Q;
    }

    public void setQuery(String query) {
        this.Query = query;
    }

    public String[] processInputQuery() throws IOException {
        String[] query = Query.split(" ");

        if (query.length != 0) {
            query = parser.processData(query);
        }
        return query;
    }

    private static boolean check(Integer[] arr, Integer toCheckValue) {
        boolean test = false;
        for (int element : arr) {
            if (element == toCheckValue) {
                test = true;
                break;
            }
        }
        return test;
    }

    public List<List<String>> Ranking() throws IOException {
        String[] processed_input = processInputQuery();
        Gson gson = new Gson();
        // final String invertedfiles_host =
        // "mongodb+srv://searchengine:mazaritaengine111222333@cluster0.pah6z.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
        DB invertedfiles_db = new DB("localhost", 27017, "SearchEngine", "InvertedFiles");
        DB urls_db = new DB("localhost", 27017, "SearchEngine", "URLS");

        Map<Integer, Double> map_ = new HashMap<Integer, Double>();
        for (int i = 0; i < processed_input.length; i++) {

            String st = processed_input[i];
            Data data = gson.fromJson(invertedfiles_db.RetrieveData(st), Data.class);
            if (data != null)
                for (int j = 0; j < data.locations.length; j++) {
                    Double tf_idf = 0.0;
                    tf_idf = data.TF[j] * (5000.0 / Double.valueOf(data.locations.length)); // tf*idf of each word in //
                    pages page = gson.fromJson(urls_db.Retrievepages(data.locations[j]), pages.class);
                    tf_idf = (tf_idf * const1) + (page.rank * const2);
                    if (map_.containsKey(data.locations[j])) {
                        Double temp = map_.get(data.locations[j]);
                        temp += tf_idf; // sum all the tf*idf values of the same location
                        map_.put(data.locations[j], temp);
                    } else
                        map_.put(data.locations[j], tf_idf);
                }
        }
        List<Entry<Integer, Double>> list = new ArrayList<>(map_.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        list.forEach(System.out::println);

        List<String> urls = new ArrayList<String>();
        List<String> title = new ArrayList<String>();
        List<String> description = new ArrayList<String>();

        for (Entry<Integer, Double> e : list)  {
            pages page = gson.fromJson(urls_db.Retrievepages(e.getKey()), pages.class);
            urls.add(page.link);
            title.add(page.title);
            description.add(page.description);
        }

        List<List<String>> l = new ArrayList<List<String>>();
        l.add(urls);
        l.add(title);
        l.add(description);
        return l; // sort the map then return the Descending sorted list
    }

    public void Phrase_Searching() throws IOException {
        String[] processed_input = processInputQuery(); // "toyota car is amazing"
        Gson gson = new Gson();
        Integer[] loc = null;
        List<Integer> intersected_loc = new ArrayList<Integer>();

        DB invertedfiles_db = new DB("localhost", 27017, "SearchEngine", "InvertedFiles");

        loc = phrases(processed_input, gson, loc, intersected_loc, invertedfiles_db);
        System.out.println(loc);
    }




    private Integer[] phrases(String[] processed_input, Gson gson, Integer[] loc, List<Integer> intersected_loc,
            DB invertedfiles_db) {
        for (int i = 0; i < processed_input.length; i++) { // loop over words [Toota ,car ,amazing]

            Data data1 = gson.fromJson(invertedfiles_db.RetrieveData(processed_input[i]), Data.class);

            if (data1 != null && i == 0) {
                loc = data1.locations;  // 0  0  0     
            }
            
            for (int k : loc) { //O(n*m*m*k*l)
                if (data1 != null && check(data1.locations, k)) // first word exists in DB in same location(doc)
                    if ((i + 1) < processed_input.length) {
                        Data data2 = gson.fromJson(invertedfiles_db.RetrieveData(processed_input[i + 1]), Data.class);
                        if (data2 != null && check(data2.locations, k)) // second word exist in DB in same location(doc)
                        {
                            int same_size = intersected_loc.size();
                            label:
                            for(int index1 : data1.frequancies[k]) {
                                for (int index2 : data2.frequancies[k])
                                    if (index1 == index2 - 1) {
                                        intersected_loc.add(k); // found common document containing words till now
                                        break label; // first found dont need to look form more entries -->choose another loc
                                    } else if (index2 > index1 + 1 || index1 > index2)
                                        break; // choose another index 1
                            }   
                            if (intersected_loc.size() == same_size && intersected_loc.size() != 0) // didnt found
                                intersected_loc.remove(k);
                            loc = intersected_loc.toArray(new Integer[0]);

                        }
                }
            }
        }
        return loc;
    }

    public static void main(String[] args) throws IOException {
        QueryProcessor q = new QueryProcessor("Temperature is a physical quantity khaled");
        q.Ranking();
        q.Phrase_Searching();
       
    }

}
