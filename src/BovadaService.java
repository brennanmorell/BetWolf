import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.*;

import java.util.*;

public class BovadaService implements WebService {
	private Document d;
	private String requestURL;
	
	public BovadaService(String r){
		requestURL = r;
	}
	
	public Map<String,Event> fetchDocument(){	
		try {
			d = Jsoup.connect(requestURL+"basketball").timeout(6000).get();
			//System.out.println(d);
			return parseDocument();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Error fetching book " + requestURL+"basketball");
			e.printStackTrace();
			return new HashMap<String, Event>();
		}
	}
	
	public HashMap<String,Event> parseDocument(){	
		HashMap<String, Event> book = new HashMap<String,Event>();
		Elements scripts = d.select("script");
	    Element target = scripts.get(19);
	    String jsonObject = target.toString().substring(54,target.toString().length()-10);
	    //System.out.println("JSON: " + jsonObject);
	    JSONObject obj = new JSONObject(jsonObject);
	    JSONArray books = obj.getJSONArray("items");

	    for(int i = 0; i < books.length(); i++){
	    	JSONObject bookObj = books.getJSONObject(i);
	    	JSONArray events = bookObj.getJSONObject("itemList").getJSONArray("items");
	    	for(int j = 0; j < events.length(); j++){
	    		JSONObject event = events.getJSONObject(j);
	    		String eventDescription = event.get("description").toString();
		    	JSONArray eventDisplayGroups = event.getJSONArray("displayGroups");
		    	JSONObject group = eventDisplayGroups.getJSONObject(0);
		    	if(group.getJSONArray("itemList").length() > 1){
			    	JSONObject moneyLine = group.getJSONArray("itemList").getJSONObject(1);
			    	JSONArray outcomes = moneyLine.getJSONArray("outcomes"); //contains all moneyline information
			    	String[] sides = eventDescription.toString().split("@");
			    	String side1 = sides[0].substring(0, sides[0].length()-1);
			    	String side2 = sides[1].substring(0, sides[1].length()-1);
			    	int outcome1 = Integer.parseInt(outcomes.getJSONObject(0).getJSONObject("price").get("american").toString());
			    	int outcome2 = Integer.parseInt(outcomes.getJSONObject(0).getJSONObject("price").get("american").toString());
			    	Event e = new Event(eventDescription.toString(), side1, side2, outcome1, outcome2, requestURL);
			    	String key = side1+"|"+side2;
			    	book.put(key, e);
		    	}
		    	else{
		    		//System.out.println("Event has multiple outcomes.");
		    	}
	    	}
	    }
	    return book;
	}

	@Override
	public void parseDocument(String bookString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printDocument() {
		// TODO Auto-generated method stub
		
	}

}
