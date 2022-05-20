package MongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import java.util.*;

import org.bson.conversions.Bson;
import org.bson.Document;

public class InvertedFiles_DB {
	public MongoClient mongoClient;
	public MongoDatabase database;
	public MongoCollection<Document> collection;
   
	public InvertedFiles_DB(String domain, int port, String db_name, String collection_name) {
		mongoClient = MongoClients.create("mongodb://" + domain + ":" + String.valueOf(port));
		database = mongoClient.getDatabase(db_name);
		collection = database.getCollection(collection_name);
	}

	public void DBSetup(String domain, int port, String db_name, String collection_name) {
		mongoClient = MongoClients.create("mongodb://" + domain + ":" + String.valueOf(port));
		database = mongoClient.getDatabase(db_name);
		collection = database.getCollection(collection_name);
	}

	public void Insert(String word) {

		Document doc = new Document("word", word);
		List<Integer> locationsList = new ArrayList<>();
		List<Double> Tfs = new ArrayList<Double>();
		List<List<Integer>> frequancies = new ArrayList<List<Integer>>();

		doc.append("locations", locationsList);
		doc.append("TF", Tfs);
		doc.append("frequancies", frequancies);

		try {
			Document temp = collection.find(Filters.eq("word", word)).first();
			if (temp.getString("word").equals(word))
				return;
		} catch (NullPointerException e) {
			collection.insertOne(doc);
		}
	}

	public void Update(String word, int location) {
		Bson filter = Filters.eq("word", word);

		try {
			// collection.findOneAndUpdate(filter, updates, options);
			collection.updateOne(filter, Updates.push("locations", location));
		} catch (MongoException me) {
			System.err.println("Unable to update due to an error: " + me);
		}
	}

	public void UpdateTF(String word, Double TF) {
		Bson filter = Filters.eq("word", word);

		try {
			collection.updateOne(filter, Updates.push("TF", TF));
		} catch (MongoException me) {
			System.err.println("Unable to update due to an error: " + me);
		}
	}

	public int getNumberOfDocuments() {
		return (int)collection.countDocuments();
	}

	public void printNumberOfDocuments() {
		System.out.println("Number of documents in <SearchEngine.InvertedFiles>: " + this.getNumberOfDocuments());
		;
	}

	public String RetrieveData(String word) {
		Document doc = collection.find(Filters.eq("word", word)).first();
		if (doc != null)
			return doc.toJson();
		else
			return null;
	}


	public void Update_freq(String word, List<Integer> freq) {
		Bson filter = Filters.eq("word", word);
		try {
			collection.updateOne(filter, Updates.push("frequancies", freq));
		} catch (MongoException me) {
			System.err.println("Unable to update due to an error: " + me);
		}
	}


	public void insert_pages(int index,String link,String description,String title, double rank)
	{
		Document doc = new Document("index", index);
		doc.append("link", link);
		doc.append("description", description);
		doc.append("title", title);
		doc.append("rank", rank);
		

		try {
			Document temp = collection.find(Filters.eq("index", index)).first();
			if (temp.getString("index").equals(index))
				return;
		} catch (NullPointerException e) {
			collection.insertOne(doc);
		}

	}


// String:link   int:index    String:description    String:title    double:rank

public String Retrievepages(int index) {
	Document doc = collection.find(Filters.eq("index", index)).first();

	if (doc != null)
		return doc.toJson();
	else
		return null;
}

}
