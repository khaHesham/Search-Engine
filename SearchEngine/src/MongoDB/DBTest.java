package MongoDB;
import MongoDB.URLs_DB;
import java.util.List;
import MongoDB.InvertedFiles_DB;
import com.google.gson.Gson;

public class DBTest {

	public static void main(String[] args) {
		// for Testing the DB

		InvertedFiles_DB invertedfiles_db = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "URLS");
		InvertedFiles_DB invertedfiles_db2 = new InvertedFiles_DB("localhost", 27017, "SearchEngine", "InvertedFiles");
		String descreption="bla bla bla blfwfsdkfs;gmls'dfgmlsdfgsd;fgkd;ksfg;a;sdfk;sf;asfa";
		String title="Comminication";
		double rank=0.2432;
		 String url="https://en.wikipedia.org/wiki/Communication";


		invertedfiles_db.insert_pages(2,url,descreption,title,rank);
		Gson gson = new Gson();
	}
}
