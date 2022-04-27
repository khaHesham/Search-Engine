package indexer;

import java.io.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.lang.Object;
import opennlp.tools.stemmer.PorterStemmer;

public class Parser {
	private String URL;
	private String[] Result;
	private boolean isParsed;
	
	public Parser(String url) {
		this.URL = url;
		this.isParsed = false;
	}
	
	public String[] getCopyOfText() {
		if (this.isParsed == false) {
			String[] message = {"NULL"};
			return message;
		}
		
		return this.Result;
	}
	
	public void setURL(String url) {
		this.URL = url;
	}
	
	public void htmlToTextParse() throws IOException {
		String[] tags = {"p", ""};
		Document doc = Jsoup.connect(this.URL).get();
		String[] myText = doc.body().text().split(" ");
		
		this.isParsed = true;
		this.Result = myText;
	}
	
	public String getOriginalText() {
		String[] words = getCopyOfText();
		String original = "";
		for (int i = 0; i < words.length - 1; i++)
			original += words[i] + " ";
		
		original += words[words.length - 1];
		
		return original;
	}
	
	public String excludeStoppingWords() {
		String original = this.getOriginalText();
		String stopwords = "";
		try {
			final String PATH = "src/indexer/stopWords.txt";
	        File file = new File(PATH);
	        Scanner myReader = new Scanner(file);
	        
	        while(myReader.hasNext()) {
	        	stopwords += " " + myReader.next() + " ";
	        	if (myReader.hasNext())
	        		stopwords += "|";
	        }
	        
	        myReader.close();
		} catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }
		
		String filteration = original.replaceAll("(" + stopwords + ")\\s*", "");
		
		return filteration;
	}
	
	public String[] getStemmedWords() {
		String[] words = this.excludeStoppingWords().split(" ");
		System.out.print("Original: ");
		for (int i = 0; i < words.length; i++)
			System.out.print(words[i] + " ");
		System.out.println("");
		String[] stemmedWords = new String[words.length];
		
		PorterStemmer stemmer = new PorterStemmer();
		
		for (int i = 0; i < words.length; i++) {
			stemmer.reset();
			for(int j = 0; j < words[i].length(); j++)
				stemmer.add(words[i].charAt(j));
			if (stemmer.stem()) {
				stemmedWords[i] = stemmer.toString();
			} else {
				System.out.println("Word {" + words[i] + "} can't be stemmed!");
			}
		}
		
		return stemmedWords;
	}
}
