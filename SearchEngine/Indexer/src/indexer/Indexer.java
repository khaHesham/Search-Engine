package indexer;
import indexer.Parser_;
//import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

public class Indexer {	

	
	public static void main(String[] args) throws IOException {
		 HashMap<String ,HashMap<String,List<Integer>>> invertedIndex=new HashMap<>();

		//loop over all urls

		String URL = "https://en.wikipedia.org/wiki/Engineer"; //will be changed with url of each page
		// "http://example.com/"
		Parser_ parser_ = new Parser_(URL);
		parser_.htmlToTextParse();
		List<String> without_unwanted_Symbols=new ArrayList<String>();
		List<String> without_stopWords=new ArrayList<String>();
		int i=0;

		/**
		[*] I know there is too much repitation but this the only way that comes inside my head for knowing the importance of each
		word and store its count also relative position in the final document 

		[*] DataBase will contain all the data listed below but in jason file instead of the long Datastructure Hashmap<....Hashmap<...>>
		i used so dont worry for the size of the code as long as i kept in mind the complixity of the whole process

		[*] it would be nicer if i added all of this in single functions in class Parser_ but i will leave it as it is till now

			.khaled.
		 */

		//for body
		String[] myText = parser_.getCopyOfText_body() /*{"test","test","test","test","test"}*/;
		if(myText[0]!="NULL")
		{
			
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(myText);	
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("body")) {   //if there is key exist -> increase count
					//i need to add counter here
					invertedIndex.get(key).get("body").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("body", new ArrayList<Integer>());
					invertedIndex.get(key).get("body").add(i);
					i++;
				}
			}
		}

		//for head words
		String[] head=parser_.getCopyOfText_head();
		if(head[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(head);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("head")) {
					//i need to add counter here
					invertedIndex.get(key).get("head").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("head", new ArrayList<Integer>() );
					invertedIndex.get(key).get("head").add(i);
					i++;
				}
			}
		}

		String[] h1=parser_.getCopyOfText_h1();
		if(h1[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h1);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("h1")) {
					//i need to add counter here
					invertedIndex.get(key).get("h1").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("h1", new ArrayList<Integer>() );
					invertedIndex.get(key).get("h1").add(i);
					i++;
				}
			}
		}
	
		String[] h2=parser_.getCopyOfText_h2();
		if(h2[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h2);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("h2")) {
					//i need to add counter here
					invertedIndex.get(key).get("h2").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("h2", new ArrayList<Integer>() );
					invertedIndex.get(key).get("h2").add(i);
					i++;
				}
			}
		}

		String[] h3=parser_.getCopyOfText_h3();
		if(h3[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h3);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("h3")) {
					//i need to add counter here
					invertedIndex.get(key).get("h3").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("h3", new ArrayList<Integer>() );
					invertedIndex.get(key).get("h3").add(i);
					i++;
				}
			}
		}


		String[] h4=parser_.getCopyOfText_h4();
		if(h4[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h4);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("h4")) {
					//i need to add counter here
					invertedIndex.get(key).get("h4").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("h4", new ArrayList<Integer>() );
					invertedIndex.get(key).get("h4").add(i);
					i++;
				}
			}
		}

		String[] div=parser_.getCopyOfText_div();
		if(div[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(div);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("div")) {
					//i need to add counter here
					invertedIndex.get(key).get("div").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("div", new ArrayList<Integer>() );
					invertedIndex.get(key).get("div").add(i);
					i++;
				}
			}
		}

		String[] footer=parser_.getCopyOfText_footer();
		if(footer[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(footer);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("footer")) {
					//i need to add counter here
					invertedIndex.get(key).get("footer").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("footer", new ArrayList<Integer>() );
					invertedIndex.get(key).get("footer").add(i);
					i++;
				}
			}
		}

		String[] title=parser_.getCopyOfText_title();
		if(title[0]!="NULL")
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(title);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).containsKey("title")) {
					//i need to add counter here
					invertedIndex.get(key).get("title").add(i);
					i++;
				}	
				else 
				{
					invertedIndex.get(key).put("title", new ArrayList<Integer>() );
					invertedIndex.get(key).get("title").add(i);
					i++;
				}
			}
	}


		int num_words=0;
		for(Map.Entry<String,HashMap<String,List<Integer>>> entry1 : invertedIndex.entrySet())   //removing unwanted symbols + stopwords is done + inverted index :D
		{			
			// Print Hashmap values
			System.out.print(entry1.getKey());
			int total_count=0;
			for (Map.Entry<String,List<Integer>> entry : entry1.getValue().entrySet())
			{
            	System.out.print(" -> " + entry.getKey() +" N: (" + entry.getValue().size()+") ");
				total_count+=entry.getValue().size();
			}
			System.out.print( " totalCount: "+total_count);
			System.out.println();
			System.out.println();
			num_words+=total_count;

		}	
		System.out.println( " total words count: "+num_words);
	}

}
