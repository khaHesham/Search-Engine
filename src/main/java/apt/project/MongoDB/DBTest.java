package apt.project.MongoDB;

import com.google.gson.Gson;

public class DBTest {

	public static void main(String[] args) {
		// for Testing the DB

		DB invertedfiles_db = new DB("localhost", 27017, "SearchEngine", "URLS");
		DB invertedfiles_db2 = new DB("localhost", 27017, "SearchEngine", "InvertedFiles");
		String descreption="bla bla bla blfwfsdkfs;gmls'dfgmlsdfgsd;fgkd;ksfg;a;sdfk;sf;asfa";
		String title="Comminication";
		double rank=0.2432;
		 String url="https://en.wikipedia.org/wiki/Communication";


		invertedfiles_db.insert_pages(4,url,descreption,title,rank);
		Gson gson = new Gson();
	}
}
