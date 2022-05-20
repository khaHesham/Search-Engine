
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import MongoDB.InvertedFiles_DB;

public class Indexer {

	public Indexer() throws IOException {
	}

	public static int Insert_into_DS(String[] final_words,
			HashMap<String, HashMap<String, List<Integer>>> invertedIndex, int i, String imp) {
		for (String key : final_words) {
			if (!invertedIndex.containsKey(key)) {
				invertedIndex.put(key, new HashMap<>());
			}
			if (invertedIndex.get(key).containsKey(imp)) {
				// i need to add counter here
				invertedIndex.get(key).get(imp).add(i);
				i++;
			} else {
				invertedIndex.get(key).put(imp, new ArrayList<Integer>());
				invertedIndex.get(key).get(imp).add(i);
				i++;
			}
		}
		return i;
	}

	public static void main(String[] args) throws IOException {
		HashMap<String, HashMap<String, List<Integer>>> invertedIndex = new HashMap<>();
		Gson gson=new Gson();
		pages page;


		// final String invertedfiles_host =
		// "mongodb+srv://searchengine:mazaritaengine111222333@cluster0.pah6z.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
		InvertedFiles_DB invertedfiles_db = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "InvertedFiles");
		InvertedFiles_DB URLS_db = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "URLS");
		
		Dictionary importance = new Hashtable();
		importance.put("head", 10.0);
		importance.put("h1", 9.0);
		importance.put("h2", 8.0);
		importance.put("h3", 7.0);
		importance.put("h4", 6.0);
		importance.put("title", 5.0);
		importance.put("div", 4.0);
		importance.put("footer", 3.0);
		importance.put("body", 2.0);

		String[] imp = { "head", "h1", "h2", "h3", "h4", "title", "div", "footer", "body" };

		for (int doc_index=0;doc_index<URLS_db.getNumberOfDocuments();doc_index++) {

			int doc_size = 0;

			page=gson.fromJson(URLS_db.Retrievepages(doc_index), pages.class);  //Retrieve from the urls database

			Parser_ parser_ = new Parser_(page.link); // a class contains all the utility functions i need for processing
			parser_.htmlToTextParse();
			int i = 0;

			/*
			 * 
			 * [*] DataBase will contain all the data listed below but in jason file
			 * instead
			 * of the long Datastructure Hashmap<....Hashmap<...>>
			 * i used so dont worry for the size of the code as long as i kept in mind the
			 * complixity of the whole process
			 *
			 * [*] We have two collections in the Database one is for crowled pages and the other for the inverted index.
			 * 
			 * .khaled.
			 */

			// Process the url.
			for (String important : imp) {
				String[] words = parser_.getCopyOfText(important);
				if (words != null) {
					String[] final_words = parser_.processData(words);
					if (final_words != null) {
						doc_size += final_words.length;
						i = Insert_into_DS(final_words, invertedIndex, i, important);
					}
				}
			}


			//Inserting in the Database part.
			for (Map.Entry<String, HashMap<String, List<Integer>>> entry1 : invertedIndex.entrySet()) {
				invertedfiles_db.Insert(entry1.getKey());
				invertedfiles_db.Update(entry1.getKey(), doc_index);
				Double tf = 0.0;
				List<Integer> indicies = new ArrayList<Integer>();
				for (Map.Entry<String, List<Integer>> entry : entry1.getValue().entrySet()) {
					tf += Double.valueOf((Double.valueOf(entry.getValue().size()) / Double.valueOf(doc_size))
							* ((Double) importance.get(entry.getKey()) / 10.0));
					for (int index : entry.getValue()) {
						indicies.add(index);
					}
				}
				invertedfiles_db.UpdateTF(entry1.getKey(), tf);
				invertedfiles_db.Update_freq(entry1.getKey(), indicies);
			}
			System.out.println(doc_index);
			invertedIndex.clear(); // to be used in another url
		}

	}
}
