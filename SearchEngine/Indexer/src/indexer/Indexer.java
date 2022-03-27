package indexer;
import indexer.Parser;

import java.io.IOException;

public class Indexer {	
	public static void main(String[] args) throws IOException {
		String URL = "https://ram-e-shop.com/product/kit-arduino-uno/";
		// "http://example.com/"
		Parser parser = new Parser(URL);
		parser.htmlToTextParse();
		String[] myText = parser.getCopyOfText();
		
		for (int i = 0; i < myText.length; i++)
			System.out.println(myText[i]);
	}

}
