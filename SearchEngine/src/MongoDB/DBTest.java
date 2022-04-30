package MongoDB;

import MongoDB.URLs_DB;
import MongoDB.InvertedFiles_DB;

public class DBTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		URLs_DB urls_db = new URLs_DB("localhost", 27017, "SearchEngine", "URLs");
		urls_db.Insert(urls_db.DataWrapper("1", "https://en.wikipedia.org/wiki/Engineer"));
		urls_db.printNumberOfDocuments();
		urls_db.printJSON(urls_db.findByID("1"));
		*/
		
		InvertedFiles_DB invertedfiles_db = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "InvertedFiles");
		invertedfiles_db.Insert(invertedfiles_db.DataWrapper("computer", "(5,187,1) (141,54523,3)"));
		invertedfiles_db.printNumberOfDocuments();
		invertedfiles_db.printJSON(invertedfiles_db.find("engineering"));
	}

}
