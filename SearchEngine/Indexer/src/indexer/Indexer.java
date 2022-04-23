package indexer;
import indexer.Parser_;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Indexer {	
	public static void main(String[] args) throws IOException {
		String URL = "https://en.wikipedia.org/wiki/Microprocessor#:~:text=A%20microprocessor%20is%20a%20computer,a%20computer's%20central%20processing%20unit.";
		// "http://example.com/"
		Parser_ parser_ = new Parser_(URL);
		parser_.htmlToTextParse();
		String[] myText = parser_.getCopyOfText();
		
		// for (int i = 0; i < myText.length; i++)
		// 	System.out.println(myText[i]);
		// //
		// parser_.extraxtMetadata(URL);

		List<String> without_stopWords=parser_.RemoveStopWords(myText, "D:\\GITHUB\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\stopwords.txt");
		

		System.out.println(without_stopWords.size());

		// for(String word:without_stopWords)   //removing stopwords is done :D
		// {
		// 	System.out.println(word);
		// }

		
		// String[] array = without_stopWords.stream().toArray(String[]::new);

		List<String> without_unwanted_Symbols=parser_.Removing_Unwanted_Symbols(without_stopWords);

		//System.out.println("without_unwanted_Symbols size : "+without_unwanted_Symbols.size());
		//step 2 removing unwanted symbol
		for(String word:without_unwanted_Symbols)   //removing stopwords is done :D
		{
			System.out.println(word);
		}
		
	}

}
