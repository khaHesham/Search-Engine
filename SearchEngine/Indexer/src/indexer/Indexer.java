package indexer;
import indexer.Parser_;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Indexer {	
	public static void main(String[] args) throws IOException {
		String URL = "https://www.imdb.com/title/tt0317219/";
		// "http://example.com/"
		Parser_ parser_ = new Parser_(URL);
		parser_.htmlToTextParse();
		String[] myText = parser_.getCopyOfText_body();

		List<String> without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(myText);	
		List<String> without_stopWords=parser_.RemoveStopWords(without_unwanted_Symbols, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
		
		for(String word:without_stopWords)   //removing unwanted symbols + stopwords is done :D
		{
			System.out.println(word);
		}
		
	}

}
