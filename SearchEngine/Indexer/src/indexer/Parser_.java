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

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import org.apache.commons.logging.Log;
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
		File metadata = new File("D:\\Executable\\Search-Engine\\SearchEngine\\Indexer\\src\\indexer\\metadata.txt");// temp file
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
				if(file.exists()){
				        
				        
				        Scanner scanner=new Scanner(file);
				        int count=0;
				        String word;
				        
				        while((word = reader.readLine()) != null) {

				            if(word.length()<=2 || word.equals("and") || word.equals("And") ||word.equals("for") || word.equals("For")) continue;

				            writer.write(word);
				            count++;
				            writer.newLine();
				        }
				        try (BufferedWriter meta = new BufferedWriter(new FileWriter(metadata))) {
							meta.write("Name: "+ tempFile.getName());
							 meta.newLine();
							 meta.write("Path: "+ tempFile.getAbsolutePath());
							 meta.newLine();
							 meta.write("Size: "+ file.length() + " bytes");
							 meta.newLine();
							 meta.write("Writeable: "+ tempFile.canWrite());
							 meta.newLine();
							 meta.write("Readable: "+ tempFile.canRead());
							 meta.newLine();
							 meta.write("Number of words: "+ count);  
							 meta.newLine();
							String content = doc.location();
							meta.write(content);
							meta.newLine();
						}			        
				    }else{
				        System.out.println("The File does not exist");
				    }
			}
			
			
			
		}
		
		
		 
		 
		 
	}
	
	
	
	
}
