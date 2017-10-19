package org.Morphia.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;



/**
 * Unit test for simple App.
 */
public class AppTest {
	
	public static void main(String[] args) {
//		List<String> result = new ArrayList<String>();
////		
////		
//		List<String> ructs =  
//				Arrays.asList("070","041",
//				"062",
//				"004",
//				"060",
//				"08072395",
//				"043",
//				"024",
//				"039",
//				"044",
//				"042",
//				"054",
//				"022");
//		
//		MongoClient mongo = new MongoClient("84.88.31.70");
//		//aggregate([{$match:{ruct:"08072395"}},{$group:{_id:"$documentTypes", total:{$sum:1}}}])
//
//		MongoCollection<Document> col = mongo.getDatabase("PRC12092017").getCollection("publication");
//		ructs.forEach(ruct->{
//			Document doc = new Document();	
//			doc.put("_id", ruct);
//			col.aggregate(
//				Arrays.asList(
//					Aggregates.match(Filters.and(Filters.eq("ruct", ruct), Filters.ne("documentTypes", "Phd Thesis"))), 
//					Aggregates.group("$documentTypes", Accumulators.sum("count", 1))))
//			.into(new ArrayList<Document>()).forEach(d->{				
//				doc.put(d.getString("_id"), d.getInteger("count"));
//			});			
//			result.add(doc.toJson());
//		});
//		
//		
//		
//		
//		System.out.print(result);
		
		System.out.println(getOrcid_publication_by_type_date());
		System.out.println(getPublicationsByDateByType());
	}
	
	private static List getOrcid_publication_by_type_date() {		
		JSONArray doc = new JSONArray(Orcid_publication_by_type_date);
		
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		
		doc.forEach(obj->{
			JSONObject json = ((JSONObject)obj);
			map.computeIfAbsent(json.getString("date"), (x -> new JSONObject())).put(json.getString("type"), json.getInt("count"));
		});		
		
		return map.entrySet()
		.stream()
		.filter(f-> f.getKey() != null)
		.sorted(Map.Entry.comparingByKey())
		.map(m-> new JSONObject(m.getValue().toString()).put("date", m.getKey()))
		.collect(Collectors.toList());		
	}
	
	private static List<JSONObject> getPublicationsByDateByType() {
		MongoClient mongo = new MongoClient("84.88.31.70");
		
		Map<String, Document> map = new HashMap<String, Document>();
		
		try {
			MongoCollection<Document> col = mongo.getDatabase("PRC12092017").getCollection("mapReducePublicationsDateType");
			
			col.find().forEach(new Block<Document>() {
				@Override
				public void apply(Document doc) {					
					map.computeIfAbsent(doc.get("_id", Document.class).getString("date"), 
							(x -> new Document())).append(doc.get("_id", Document.class).getString("type"), doc.getDouble("value"));					
				}
			});
			
			return map.entrySet().stream()
					.filter(f-> f.getKey() != null)
					.sorted(Map.Entry.comparingByKey())
					.map(m-> new JSONObject(m.getValue().toJson()).put("date", m.getKey()))
					.collect(Collectors.toList());
		}finally {
			mongo.close();
		}
		
	}
	
	
	private static String Orcid_publication_by_type_date = "[{\"type\":\"Phd Thesis\",\"count\":1,\"date\":\"2004\"},{\"type\":\"Journal Article\",\"count\":4,\"date\":\"2016\"},{\"type\":\"Journal Article\",\"count\":1,\"date\":\"2014\"},{\"type\":\"Journal Article\",\"count\":4,\"date\":\"2013\"},{\"type\":\"Journal Article\",\"count\":4,\"date\":\"2012\"},{\"type\":\"Chapter in Book\",\"count\":1,\"date\":\"2007\"},{\"type\":\"Chapter in Book\",\"count\":1,\"date\":\"2005\"},{\"type\":\"Chapter in Book\",\"count\":3,\"date\":\"2011\"},{\"type\":\"Chapter in Book\",\"count\":3,\"date\":\"2006\"},{\"type\":\"Chapter in Book\",\"count\":3,\"date\":\"1994\"},{\"type\":\"Journal Article\",\"count\":7,\"date\":\"2011\"},{\"type\":\"Book\",\"count\":1,\"date\":\"2013\"},{\"type\":\"Journal Article\",\"count\":3,\"date\":\"2010\"},{\"type\":\"Chapter in Book\",\"count\":1,\"date\":\"1996\"},{\"type\":\"Chapter in Book\",\"count\":1,\"date\":\"2014\"},{\"type\":\"Chapter in Book\",\"count\":6,\"date\":\"2009\"},{\"type\":\"Journal Article\",\"count\":2,\"date\":\"2015\"},{\"type\":\"Chapter in Book\",\"count\":3,\"date\":\"2004\"},{\"type\":\"Chapter in Book\",\"count\":1,\"date\":\"2013\"},{\"type\":\"Chapter in Book\",\"count\":2,\"date\":\"2015\"},{\"type\":\"Chapter in Book\",\"count\":6,\"date\":\"2016\"},{\"type\":\"Journal Article\",\"count\":1,\"date\":\"2006\"},{\"type\":\"Journal Article\",\"count\":3,\"date\":\"2007\"},{\"type\":\"Chapter in Book\",\"count\":1,\"date\":\"2012\"},{\"type\":\"Journal Article\",\"count\":1,\"date\":\"2008\"},{\"type\":\"Chapter in Book\",\"count\":5,\"date\":\"2010\"},{\"type\":\"Journal Article\",\"count\":1,\"date\":\"2002\"},{\"type\":\"Journal Article\",\"count\":1,\"date\":\"2009\"}]";
}
