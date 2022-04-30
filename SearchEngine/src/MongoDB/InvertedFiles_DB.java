package MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.*;

import org.bson.Document;

public class InvertedFiles_DB {
	public MongoClient mongoClient;
	public MongoDatabase database;
	public MongoCollection<Document> collection;
	
	public InvertedFiles_DB(String domain, int port, String db_name, String collection_name) {
		mongoClient = MongoClients.create("mongodb://" + domain +":" + String.valueOf(port));
		database = mongoClient.getDatabase(db_name);
		collection = database.getCollection(collection_name);
	}

	public void DBSetup(String domain, int port, String db_name, String collection_name) {
		mongoClient = MongoClients.create("mongodb://" + domain +":" + String.valueOf(port));
		database = mongoClient.getDatabase(db_name);
		collection = database.getCollection(collection_name);
	}
	
	public Map<String, String> DataWrapper(String word, String locations) {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("word", word);
		data.put("locations", locations);
		
		return data;
	}
	
	public void Insert(Map<String, String> data) {
		
		Document doc = new Document("word", data.get("word"));
		doc.append("locations",data.get("locations"));
		
		try {
			Document temp = collection.find(Filters.eq("word", data.get("word"))).first();
			if (temp.getString("word").equals(doc.get("word")))
				return;//System.out.println("Word '" + doc.get("word") + "' is already exist, you can update it but not duplicate it!");
		} catch (NullPointerException e) {
			collection.insertOne(doc);
		}
	}
	
	public long getNumberOfDocuments() {
		return collection.countDocuments();
	}
	
	public void printNumberOfDocuments() {
		System.out.println("Number of documents in <SearchEngine.InvertedFiles>: " + this.getNumberOfDocuments());;
	}
	
	public Map<String, String> find(String word) {
		Map<String, String> data = new HashMap<String, String>();
		
		try {
			Document doc = collection.find(Filters.eq("word", word)).first();
			data.put("_id", doc.getObjectId("_id").toString());
			data.put("word", doc.getString("word"));
			data.put("locations", doc.getString("locations"));
		} catch (NullPointerException e) {
			System.out.println("{\nError: 404,\nWord <" + word + ">: Not Found\n}");
			
			data.put("Error", "404");
		}
		
		return data;
	}
	
	public void printJSON(Map<String, String> data) {
		if (data.get("Error") != null)
			return;
		else if (data.get("_id") != null)
			System.out.println("{\n'_id': " + data.get("_id") + ",\n'word': " + data.get("word") + ",\n'locations': " + data.get("locations") + "\n}");
		if (data.get("_id") == null)
			System.out.println("{\n'word': " + data.get("word") + ",\n'locations': " + data.get("locations") + "\n}");
	}


	public void Update(String word,String locations) {
		
		collection.updateOne(Filters.eq("word",word), Updates.set("locations", locations));
	}


}
