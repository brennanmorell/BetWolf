import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class WebService {
	private Document d;
	private String bookName;
	private String sport;
	public WebService(String b, String s){
		bookName = b;
		sport = s;
	}
	
	public Map<String,List<Event>> fetch(){
		try {
			d = Jsoup.connect("http://www.oddsshark.com/"+bookName+"/odds").timeout(6000).get();
			//System.out.println(d);
			return parseDocument();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return new HashMap<String, List<Event>>();
	}
	
	public Map<String,List<Event>> parseDocument(){
		Map<String, List<Event>> aggregateBook = new HashMap<String, List<Event>>();
		List<String> eventNames = new ArrayList<String>();
		List<String> sources = new ArrayList<String>();
		
		//GET SOURCES
		Elements bookHeaders = d.select(".op-book-header");
		for(Element bookHeader : bookHeaders){
			Element img = bookHeader.child(0).child(0);
			sources.add(img.attr("alt"));
		}
		
		//GET EVENTS 
		Elements events = d.select(".op-matchup-wrapper."+sport);
		for(Element event : events){
			Elements teams = event.select(".op-matchup-team.op-matchup-text");
			Element team1 = teams.get(0).child(0);
			Element team2 = teams.get(1).child(0);
			if(team1.children().size() > 1 && team2.children().size() > 1){
				team1 = team1.child(0);
				team2 = team2.child(0);
			}
			eventNames.add(team1.text()+" vs "+team2.text()); //fix hash and store team names in values
		}
		
		//GET EVENT ODDS FROM ALL SOURCES
		Elements eventLines = d.select(".op-item-row-wrapper.not-futures");
		int eventIndex = 0;
		for(int i = 0; i < eventLines.size(); i++){
			Elements siteOdds = eventLines.get(i).select(".op-item-wrapper");
			List<Event> eventList = new ArrayList<Event>();
			String eventName = eventNames.get(eventIndex);
			for(int j = 0; j < siteOdds.size(); j++){
				String spreadClass = ".op-item.spread-price";
				if(sport.equals("fighting")){
					spreadClass = ".op-item.op-spread";
				}
				Elements spreadElems = siteOdds.get(j).select(spreadClass);
				if(spreadElems.get(0).hasText()){
					int side1 = Integer.parseInt(spreadElems.get(0).text());
					int side2 = Integer.parseInt(spreadElems.get(1).text());
					Event e = new Event(eventName, side1, side2, sources.get(j));
					eventList.add(e);
				}
				else{
					String moneyLine1 = spreadElems.get(0).attr("data-op-moneyline");
					String moneyLine2 = spreadElems.get(1).attr("data-op-moneyline");
					if(sport.equalsIgnoreCase("fighting")){
						String sideStr1 = moneyLine1.substring(moneyLine1.indexOf(":")+2,moneyLine1.length()-2);
						String sideStr2 = moneyLine2.substring(moneyLine2.indexOf(":")+2,moneyLine2.length()-2);
						if(!sideStr1.equals("") && !sideStr2.equals("")){
							int side1 = Integer.parseInt(sideStr1);
							int side2 = Integer.parseInt(sideStr2);
							Event e = new Event(eventName, side1, side2, sources.get(j));
							eventList.add(e);
						}
					}
				}
			}
			aggregateBook.put(eventName, eventList);
			eventIndex++;
		}
		
		return aggregateBook;
	}
}
