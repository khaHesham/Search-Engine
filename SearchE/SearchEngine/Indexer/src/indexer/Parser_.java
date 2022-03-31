package indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class Parser_ {
	private String URL;
	private String[] Result;
	private boolean isParsed;
	Document doc;
	
	public Parser_(String url) {
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
		doc = Jsoup.connect(this.URL).get();
		String[] myText = doc.body().text().split(" ");
		
		
		
		this.isParsed = true;
		this.Result = myText;
	}
	
	public void extraxtMetadata(String path) throws IOException
	{
		File file=new File("D:\\Executable\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\test.txt");//send path here
		File tempFile = new File("D:\\Executable\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\temptest.txt");// temp file
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
				if(file.exists()){
				        
				        
				        Scanner scanner=new Scanner(file);
				        int count=0;
				        String word;
				        
				        while((word = reader.readLine()) != null) {

				            if(word.length()<=2 || word=="and" || word=="And" ||word=="for" || word=="For") continue;

				            writer.write(word);
				            count++;
				            writer.newLine();
				        }
				        System.out.println("Name: "+ tempFile.getName());
				        System.out.println("Path: "+ tempFile.getAbsolutePath());
				        System.out.println("Size: "+ file.length() + " bytes");
				        System.out.println("Writeable: "+ tempFile.canWrite());
				        System.out.println("Readable: "+ tempFile.canRead());
				        System.out.println("Number of words: "+ count);  				        
				    }else{
				        System.out.println("The File does not exist");
				    }
			}
		}
		 
		 
		 
	}
	
	
	
	
}
