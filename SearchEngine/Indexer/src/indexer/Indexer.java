package indexer;
import indexer.Parser_;

import java.io.IOException;

public class Indexer {	
	public static void main(String[] args) throws IOException {
		String URL = "https://ram-e-shop.com/product/kit-arduino-uno/";
		// "http://example.com/"
		Parser_ parser_ = new Parser_(URL);
		parser_.htmlToTextParse();
		String[] myText = parser_.getCopyOfText();
		
		for (int i = 0; i < myText.length; i++)
			System.out.println(myText[i]);
		parser_.extraxtMetadata(URL);
	}

}
