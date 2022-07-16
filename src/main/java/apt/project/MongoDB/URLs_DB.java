package apt.project.MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class URLs_DB {
	public MongoClient mongoClient;
	public MongoDatabase database;
	public MongoCollection<Document> collection;
	
	URLs_DB(String domain, int port, String db_name, String collection_name) {
		mongoClient = MongoClients.create("mongodb://" + domain +":" + String.valueOf(port));
		database = mongoClient.getDatabase(db_name);
		collection = database.getCollection(collection_name);
	}
	
	public void DBSetup(String domain, int port, String db_name, String collection_name) {
		mongoClient = MongoClients.create("mongodb://" + domain +":" + String.valueOf(port));
		database = mongoClient.getDatabase(db_name);
		collection = database.getCollection(collection_name);
	}
	
	public Map<String, String> DataWrapper(String id, String url) {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("id", id);
		data.put("url", url);
		
		return data;
	}
	
	public void Insert(Map<String, String> data) {
		
		Document doc = new Document("id", data.get("id"));
		doc.append("url",data.get("url"));
		
		try {
			Document temp = collection.find(Filters.eq("id", data.get("id"))).first();
			if (temp.getString("url").equals(doc.get("url")))
				System.out.println("URL <" + doc.get("url") + "> is already exist, you can update it but not duplicate it!");
		} catch (NullPointerException e) {
			collection.insertOne(doc);
		}
	}
	
	public long getNumberOfDocuments() {
		return collection.countDocuments();
	}
	
	public void printNumberOfDocuments() {
		System.out.println("Number of documents in <SearchEngine.URLs>: " + this.getNumberOfDocuments());;
	}
	
	public Map<String, String> findByID(String id) {
		Map<String, String> data = new HashMap<String, String>();
		
		try {
			Document doc = collection.find(Filters.eq("id", id)).first();
			data.put("_id", doc.getObjectId("_id").toString());
			data.put("id", doc.getString("id"));
			data.put("url", doc.getString("url"));
		} catch (NullPointerException e) {
			System.out.println("{\nError: 404,\nURL of id <" + id + ">: Not Found\n}");
			
			data.put("Error", "404");
		}
		
		return data;
	}
	
	public Map<String, String> findByURL(String url) {
		Map<String, String> data = new HashMap<String, String>();
		
		try {
			Document doc = collection.find(Filters.eq("url", url)).first();
			data.put("_id", doc.getObjectId("_id").toString());
			data.put("id", doc.getString("id"));
			data.put("url", doc.getString("url"));
		} catch (NullPointerException e) {
			System.out.println("{\nError: 404,\nURL <" + url + ">: Not Found\n}");
			
			data.put("Error", "404");
		}
		
		return data;
	}
	
	public void printJSON(Map<String, String> data) {
		if (data.get("Error") != null)
			return;
		else if (data.get("_id") != null)
			System.out.println("{\n'_id': " + data.get("_id") + ",\n'id': " + data.get("id") + ",\n'url': " + data.get("url") + "\n}");
		if (data.get("_id") == null)
			System.out.println("{\n'id': " + data.get("id") + ",\n'url': " + data.get("url") + "\n}");
	}
}
