//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class QueryProcessor{
//
//    Parser_ parser ;
//    List<String>Query;
//
//    public QueryProcessor()
//    {
////        parser = new Parser_();
////        Query=new ArrayList<String>();
//    }
//    public void getQuery(String query_file_path) throws IOException {
//        File file = new File(query_file_path);
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        String word_;
//        while ((word_ = reader.readLine()) != null) {
//            Query.add(word_);
//        }
//        reader.close();
//
//        PrintWriter writer = new PrintWriter(file);
//        writer.print("");
//        writer.close();
//
//    }
//
//    public String [] processInputQuery() throws IOException {
//        String [] query=new String[Query.size()];
//
//        if(Query.size()!=0) {
//            query = parser.processData((String[]) Query.toArray());
//        }
//        return query;
//    }
//
//
//}
////public class  {
////
////
////
////}
