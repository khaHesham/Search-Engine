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
		 HashMap<String ,HashMap<String,Integer>> invertedIndex=new HashMap<>();

		//loop over all urls

		String URL = "https://blog.reedsy.com/repetition-examples/"; //will be changed with url of each page
		// "http://example.com/"
		Parser_ parser_ = new Parser_(URL);
		parser_.htmlToTextParse();
		List<String> without_unwanted_Symbols=new ArrayList<String>();
		List<String> without_stopWords=new ArrayList<String>();

		//for body
		String[] myText = parser_.getCopyOfText_body();
		if(myText.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(myText);	
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("body")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("body").intValue();
					invertedIndex.get(key).remove("body");
					i++;
					invertedIndex.get(key).put("body", i);
				}	
				else 
					invertedIndex.get(key).put("body", 1);
			}
		}

		//for head words
		String[] head=parser_.getCopyOfText_head();
		if(head.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(head);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("head")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("head").intValue();
					invertedIndex.get(key).remove("head");
					i++;
					invertedIndex.get(key).put("head", i);
				}	
				else 
					invertedIndex.get(key).put("head", 1);
			}
		}

		String[] h1=parser_.getCopyOfText_h1();
		if(h1.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h1);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("h1")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("h1").intValue();
					invertedIndex.get(key).remove("h1");
					i++;
					invertedIndex.get(key).put("h1", i);
				}	
				else 
					invertedIndex.get(key).put("h1", 1);
			}
		}
	
		String[] h2=parser_.getCopyOfText_h2();
		if(h2.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h2);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("h2")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("h2").intValue();
					invertedIndex.get(key).remove("h2");
					i++;
					invertedIndex.get(key).put("h2", i);
				}	
				else 
					invertedIndex.get(key).put("h2", 1);
			}
		}

		String[] h3=parser_.getCopyOfText_h3();
		if(h3.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h3);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("h3")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("h3").intValue();
					invertedIndex.get(key).remove("h3");
					i++;
					invertedIndex.get(key).put("h3", i);
				}	
				else 
					invertedIndex.get(key).put("h3", 1);
			}
		}


		String[] h4=parser_.getCopyOfText_h4();
		if(h4.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(h4);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("h4")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("h4").intValue();
					invertedIndex.get(key).remove("h4");
					i++;
					invertedIndex.get(key).put("h4", i);
				}	
				else 
					invertedIndex.get(key).put("h4", 1);
			}
		}

		String[] div=parser_.getCopyOfText_div();
		if(div.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(div);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("div")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("div").intValue();
					invertedIndex.get(key).remove("div");
					i++;
					invertedIndex.get(key).put("div", i);
				}	
				else 
					invertedIndex.get(key).put("div", 1);
			}
		}

		String[] footer=parser_.getCopyOfText_footer();
		if(footer.length>0)
		{
			without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(footer);
			without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
			for (String key : without_stopWords) {
				if (!invertedIndex.containsKey(key)) {
					invertedIndex.put(key, new HashMap<>());
				}	
				if (invertedIndex.get(key).equals("footer")) {
					//i need to add counter here
					int i=invertedIndex.get(key).get("footer").intValue();
					invertedIndex.get(key).remove("footer");
					i++;
					invertedIndex.get(key).put("footer", i);
				}	
				else 
					invertedIndex.get(key).put("footer", 1);
			}
		}

	// 	String[] title=parser_.getCopyOfText_title();
	// 	if(title.length>0)
	// 	{
	// 		without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(title);
	// 		without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
	// 		for (String key : without_stopWords) {
	// 			if (!invertedIndex.containsKey(key)) {
	// 				invertedIndex.put(key, new HashMap<>());
	// 			}	
	// 			if (invertedIndex.get(key).equals("title")) {
	// 				//i need to add counter here
	// 				int i=invertedIndex.get(key).get("title").intValue();
	// 				invertedIndex.get(key).put("title", i+1);
	// 			}	
	// 			else 
	// 				invertedIndex.get(key).put("title", 1);
	// 		}
	// }



		for(Map.Entry<String,HashMap<String,Integer>> entry1 : invertedIndex.entrySet())   //removing unwanted symbols + stopwords is done :D
		{			
			// Print Hashmap values
			System.out.print(entry1.getKey());
			for (Map.Entry<String,Integer> entry : entry1.getValue().entrySet())
            	System.out.println(" -> " + entry.getKey() +" N:(" + entry.getValue()+")");
			System.out.println();
			//System.out.print(word+" : "+invertedIndex.get("ice").en + "->");						
		}

		
	}

}
