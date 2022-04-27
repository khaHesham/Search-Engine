package indexer;
import java.io.IOException;

import indexer.Parser;
public class Parser_Stemmer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String URL = "https://en.wikipedia.org/wiki/Engineer"; //will be changed with url of each page
		Parser parser = new Parser(URL);
		try {
			parser.htmlToTextParse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// System.out.println("Orignial: " + parser.getOriginalText());
		// System.out.println("Filteration: " + parser.excludeStoppingWords());
		System.out.println("Stemming: " + parser.getStemmedWords());
	}

}
