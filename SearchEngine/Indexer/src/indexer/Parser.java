package indexer;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
}
