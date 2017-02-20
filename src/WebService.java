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
	
	public Map<String,List<Spread>> fetch(int outcomes){
		try {
			String url = "http://www.oddsshark.com/"+bookName+"/odds";		
			d = Jsoup.connect(url).timeout(6000).get();
			//System.out.println(d);
			return parseDocument(outcomes);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return new HashMap<String, List<Spread>>();
	}
	
	public Map<String,List<Spread>> parseDocument(int outcomes){
		Map<String, List<Spread>> aggregateBook = new HashMap<String, List<Spread>>();
		List<String> eventNames = new ArrayList<String>();
		List<String> sources = new ArrayList<String>();
		
		//GET SOURCES
		Elements bookHeaders = d.select(".op-book-header.no-vegas");
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
			Elements siteOdds = eventLines.get(i).select(".op-item-wrapper.no-vegas");
			List<Spread> eventList = new ArrayList<Spread>();
			String eventName = eventNames.get(eventIndex);
			for(int j = 0; j < siteOdds.size(); j++){
				String spreadClass = ".op-item.spread-price";
				if(sport.equals("fighting") || sport.equals("tennis")){
					spreadClass = ".op-item.op-spread";
				}
				Elements spreadElems = siteOdds.get(j).select(spreadClass); 
				if(spreadElems.size() > 1){ //ncaab had one event that had one side with .op-item.spread-price class, and one with .op-item.op-spread
					List<Integer> sides = new ArrayList<Integer>();
					if(spreadElems.get(0).hasText()){
						int side1 = Integer.parseInt(spreadElems.get(0).text());
						int side2 = Integer.parseInt(spreadElems.get(1).text());
						sides.add(side1);
						sides.add(side2);
						if(!sources.get(j).equals("Opening")){
							Spread e = new Spread(eventName, sides, sources.get(j));
							eventList.add(e);
						}
					}
					else{
						String moneyLine1 = spreadElems.get(0).attr("data-op-moneyline");
						String moneyLine2 = spreadElems.get(1).attr("data-op-moneyline");
						if(sport.equals("fighting") || sport.equals("tennis")){
							String sideStr1 = moneyLine1.substring(moneyLine1.indexOf(":")+2,moneyLine1.length()-2);
							String sideStr2 = moneyLine2.substring(moneyLine2.indexOf(":")+2,moneyLine2.length()-2);
							if(!sideStr1.equals("") && !sideStr2.equals("")){
								int side1 = Integer.parseInt(sideStr1);
								int side2 = Integer.parseInt(sideStr2);
								sides.add(side1);
								sides.add(side2);
								if(!sources.get(j).equals("Opening")){
									Spread e = new Spread(eventName, sides, sources.get(j));
									eventList.add(e);
								}
							}
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