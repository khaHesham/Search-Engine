
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import MongoDB.URLs_DB;
import MongoDB.InvertedFiles_DB;

public class Indexer {

	public Indexer() throws IOException {
	}

	public static void main(String[] args) throws IOException {
		HashMap<String, HashMap<String, List<Integer>>> invertedIndex = new HashMap<>();
		int doc_index = 0;

		// loop over all urls

		String[] urls = new String[10]; // will be changed with url of each page
		urls[0] = "https://en.wikipedia.org/wiki/Temperature";
		urls[1] = "https://en.wikipedia.org/wiki/Engineering";
		urls[2] = "https://en.wikipedia.org/wiki/Communication";
		urls[3] = "https://en.wikipedia.org/wiki/Food";
		urls[4] = "https://en.wikipedia.org/wiki/Animal";
		urls[5] = "https://en.wikipedia.org/wiki/Sport";
		urls[6] = "https://en.wikipedia.org/wiki/Law";
		urls[7] = "https://en.wikipedia.org/wiki/Egypt";
		urls[8] = "https://en.wikipedia.org/wiki/Adidas";
		urls[9] = "https://en.wikipedia.org/wiki/ML_(programming_language)";

		String stopWords_PATH = "C:\\Users\\khaled\\Desktop\\indexo\\src\\stopwords.txt";
		InvertedFiles_DB invertedfiles_db = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "InvertedFiles");

		Dictionary importance = new Hashtable();
		importance.put("head", 10);
		importance.put("h1", 9);
		importance.put("h2", 8);
		importance.put("h3", 7);
		importance.put("h4", 6);
		importance.put("title", 5);
		importance.put("div", 4);
		importance.put("footer", 3);
		importance.put("body", 2);

		// for (String URL : urls) {

		// // "http://example.com/"
		// Parser_ parser_ = new Parser_(URL);
		// parser_.htmlToTextParse();
		// int i = 0;

		// /**
		// * [*] I know there is too much repitation but this the only way that comes
		// * inside my head for knowing the importance of each
		// * word and store its count also relative position in the final document
		// *
		// * [*] DataBase will contain all the data listed below but in jason file
		// * instead
		// * of the long Datastructure Hashmap<....Hashmap<...>>
		// * i used so dont worry for the size of the code as long as i kept in mind the
		// * complixity of the whole process
		// *
		// * [*] it would be nicer if i added all of this in single functions in class
		// * Parser_ but i will leave it as it is till now
		// *
		// * .khaled.
		// */

		// // for body
		// String[] myText = parser_.getCopyOfText_body();
		// if (myText[0] != "NULL") {

		// String[] final_words = parser_.processData(myText);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("body")) { // if there is key exist ->
		// // increase count
		// // i need to add counter here
		// invertedIndex.get(key).get("body").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("body", new ArrayList<Integer>());
		// invertedIndex.get(key).get("body").add(i);
		// i++;
		// }
		// }
		// }
		// }

		// i = 0;
		// // for head words
		// String[] head = parser_.getCopyOfText_head();
		// if (head[0] != "NULL") {
		// String[] final_words = parser_.processData(head);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("head")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("head").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("head", new ArrayList<Integer>());
		// invertedIndex.get(key).get("head").add(i);
		// i++;
		// }
		// }
		// }
		// }
		// i = 0;
		// String[] h1 = parser_.getCopyOfText_h1();
		// if (h1[0] != "NULL") {
		// String[] final_words = parser_.processData(h1);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("h1")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("h1").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("h1", new ArrayList<Integer>());
		// invertedIndex.get(key).get("h1").add(i);
		// i++;
		// }
		// }
		// }
		// }
		// i = 0;
		// String[] h2 = parser_.getCopyOfText_h2();
		// if (h2[0] != "NULL") {
		// String[] final_words = parser_.processData(h2);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("h2")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("h2").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("h2", new ArrayList<Integer>());
		// invertedIndex.get(key).get("h2").add(i);
		// i++;
		// }
		// }
		// }
		// }
		// i = 0;
		// String[] h3 = parser_.getCopyOfText_h3();
		// if (h3[0] != "NULL") {
		// String[] final_words = parser_.processData(h3);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("h3")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("h3").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("h3", new ArrayList<Integer>());
		// invertedIndex.get(key).get("h3").add(i);
		// i++;
		// }
		// }
		// }
		// }

		// i = 0;
		// String[] h4 = parser_.getCopyOfText_h4();
		// if (h4[0] != "NULL") {
		// String[] final_words = parser_.processData(h4);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("h4")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("h4").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("h4", new ArrayList<Integer>());
		// invertedIndex.get(key).get("h4").add(i);
		// i++;
		// }
		// }
		// }
		// }
		// i = 0;
		// String[] div = parser_.getCopyOfText_div();
		// if (div[0] != "NULL") {
		// String[] final_words = parser_.processData(div);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("div")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("div").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("div", new ArrayList<Integer>());
		// invertedIndex.get(key).get("div").add(i);
		// i++;
		// }
		// }
		// }
		// }
		// i = 0;
		// String[] footer = parser_.getCopyOfText_footer();
		// if (footer[0] != "NULL") {
		// String[] final_words = parser_.processData(footer);
		// if (final_words != null) {
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("footer")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("footer").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("footer", new ArrayList<Integer>());
		// invertedIndex.get(key).get("footer").add(i);
		// i++;
		// }
		// }
		// }
		// }
		// i = 0;
		// String[] title = parser_.getCopyOfText_title();
		// if (title[0] != "NULL") {
		// String[] final_words = parser_.processData(title);
		// for (String key : final_words) {
		// if (!invertedIndex.containsKey(key)) {
		// invertedIndex.put(key, new HashMap<>());
		// }
		// if (invertedIndex.get(key).containsKey("title")) {
		// // i need to add counter here
		// invertedIndex.get(key).get("title").add(i);
		// i++;
		// } else {
		// invertedIndex.get(key).put("title", new ArrayList<Integer>());
		// invertedIndex.get(key).get("title").add(i);
		// i++;
		// }
		// }
		// }

		// for (Map.Entry<String, HashMap<String, List<Integer>>> entry1 :
		// invertedIndex.entrySet()) {
		// invertedfiles_db.Insert(invertedfiles_db.DataWrapper(entry1.getKey(), ""));
		// for (Map.Entry<String, List<Integer>> entry : entry1.getValue().entrySet()) {
		// Map<String, String> data = invertedfiles_db.find(entry1.getKey());
		// invertedfiles_db.Update(entry1.getKey(), data.get("locations") + " (" +
		// doc_index + ","
		// + entry.getValue().size() + "," + importance.get(entry.getKey()).toString() +
		// ")");
		// }

		// }

		// System.out.println(doc_index);
		// doc_index++;
		// invertedIndex.clear();
		//}

		Map<String, String> data = invertedfiles_db.find("engin");
		String[] indecies = data.get("locations").split(" ");

		for (String s : indecies)
			System.out.println(s);

	}
}
