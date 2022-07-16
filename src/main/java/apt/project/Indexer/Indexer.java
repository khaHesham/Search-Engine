package apt.project.Indexer;

import apt.project.MongoDB.DB;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;
public class Indexer extends Thread{

	private final UrlQueue urls;

	public Indexer(UrlQueue urls){
		this.urls = urls;
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

	public void run() {
		HashMap<String, HashMap<String, List<Integer>>> invertedIndex = new HashMap<>();

		DB invertedfiles_db = new DB("localhost", 27017, "SearchEngine", "InvertedFiles");
		DB URLS_db = new DB("localhost", 27017, "SearchEngine", "URLS");

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

		String[] imp = {"head", "h1", "h2", "h3", "h4", "title", "div", "footer", "body"};


		while(!urls.isEmpty())
		{
			int doc_size = 0;

			int doc_index = urls.getdoc_index();
			String url = urls.FetchUrl();

			Parser_ parser_ = new Parser_(url); // a class contains all the utility functions i need for processing
			try {
				parser_.htmlToTextParse();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
					String[] final_words = new String[0];
					try {
						final_words = parser_.processData(words);
					} catch (IOException e) {
						e.printStackTrace();
					}
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
				double tf = 0.0;
				List<Integer> indicies = new ArrayList<Integer>();
				for (Map.Entry<String, List<Integer>> entry : entry1.getValue().entrySet()) {
					tf += ((double) entry.getValue().size() / (double) doc_size)
							* ((Double) importance.get(entry.getKey()) / 10.0);
					indicies.addAll(entry.getValue());
				}
				invertedfiles_db.UpdateTF(entry1.getKey(), tf);
				invertedfiles_db.Update_freq(entry1.getKey(), indicies);
			}
			System.out.println(doc_index);
			invertedIndex.clear(); // to be used in another url
		}
	}
}
