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
					if(sport.equals("fighting")){
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
	
	public Map<String, List<Event>> spoofResults(){
		Map<String, List<Event>> spoofBook = new HashMap<String, List<Event>>();
		
		List<Event> events = new ArrayList<Event>();
		String key = "Deontay Wilder vs Gerald Washington";
		Event e = new Event(key, Integer.parseInt("-1667"), Integer.parseInt("800"), "Opening");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Deontay Wilder vs Gerald Washington";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-1400"), Integer.parseInt("750"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1500"), Integer.parseInt("1000"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1500"), Integer.parseInt("1000"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "David Haye vs Tony Bellew";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-625"), Integer.parseInt("365"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-800"), Integer.parseInt("500"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-800"), Integer.parseInt("550"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-800"), Integer.parseInt("550"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Eleider Alvarez vs Lucian Bute";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-588"), Integer.parseInt("360"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-335"), Integer.parseInt("245"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-335"), Integer.parseInt("245"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Gary Russell Jr vs Oscar Escandon";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-5000"), Integer.parseInt("1400"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-5250"), Integer.parseInt("1750"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-5250"), Integer.parseInt("1750"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Miguel Angel Cotto vs James Kirkland";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-500"), Integer.parseInt("300"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-500"), Integer.parseInt("350"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-420"), Integer.parseInt("350"), "Westgate");
		events.add(e);
		e = new Event(key, Integer.parseInt("-460"), Integer.parseInt("360"), "BetOnline");
		events.add(e);
		e = new Event(key, Integer.parseInt("-445"), Integer.parseInt("345"), "MyBookie");
		events.add(e);
		e = new Event(key, Integer.parseInt("-420"), Integer.parseInt("375"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-450"), Integer.parseInt("325"), "TopBet");
		events.add(e);
		e = new Event(key, Integer.parseInt("-440"), Integer.parseInt("350"), "BetNow");
		events.add(e);
		e = new Event(key, Integer.parseInt("-460"), Integer.parseInt("360"), "SportsBetting");
		events.add(e);
		e = new Event(key, Integer.parseInt("-420"), Integer.parseInt("375"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Francisco Vargas vs Miguel Berchelt";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-303"), Integer.parseInt("900"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-135"), Integer.parseInt("105"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-135"), Integer.parseInt("115"), "BetOnline");
		events.add(e);
		e = new Event(key, Integer.parseInt("-125"), Integer.parseInt("105"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-133"), Integer.parseInt("105"), "GTBets");
		events.add(e);
		e = new Event(key, Integer.parseInt("-35"), Integer.parseInt("115"), "SportsBetting");
		events.add(e);
		e = new Event(key, Integer.parseInt("-125"), Integer.parseInt("105"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Gavin McDonnell vs Rey Vargas";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("144"), Integer.parseInt("-196"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("210"), Integer.parseInt("-270"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("220"), Integer.parseInt("-260"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("220"), Integer.parseInt("-260"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Jermell Charlo vs Charles Hatley";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-1667"), Integer.parseInt("650"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1000"), Integer.parseInt("600"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1000"), Integer.parseInt("675"), "BetOnline");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1200"), Integer.parseInt("775"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1000"), Integer.parseInt("675"), "SportsBettting");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1200"), Integer.parseInt("775"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Guillermo Rigondeaux vs Moises Flores";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-1000"), Integer.parseInt("500"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-800"), Integer.parseInt("500"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-900"), Integer.parseInt("500"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-900"), Integer.parseInt("500"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Petteri Fröjdholm vs Faroukh Kourbanov";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-400"), Integer.parseInt("335"), "Opening");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Scott Cardle vs Luke Campbell";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("440"), Integer.parseInt("-769"), "Opening");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Adrien Broner vs Adrian Granados";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-500"), Integer.parseInt("300"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-350"), Integer.parseInt("250"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-360"), Integer.parseInt("300"), "Westgate");
		events.add(e);
		e = new Event(key, Integer.parseInt("-310"), Integer.parseInt("260"), "BetOnline");
		events.add(e);
		e = new Event(key, Integer.parseInt("-315"), Integer.parseInt("255"), "MyBookie");
		events.add(e);
		e = new Event(key, Integer.parseInt("-300"), Integer.parseInt("270"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-300"), Integer.parseInt("220"), "TopBet");
		events.add(e);
		e = new Event(key, Integer.parseInt("-310"), Integer.parseInt("255"), "BetNow");
		events.add(e);
		e = new Event(key, Integer.parseInt("-310"), Integer.parseInt("260"), "SportsBetting");
		events.add(e);
		e = new Event(key, Integer.parseInt("-300"), Integer.parseInt("270"), "SportBet");
		events.add(e);
		e = new Event(key, Integer.parseInt("-360"), Integer.parseInt("280"), "Station");
		events.add(e);
		e = new Event(key, Integer.parseInt("-380"), Integer.parseInt("300"), "Mirage");
		events.add(e);
		e = new Event(key, Integer.parseInt("-340"), Integer.parseInt("280"), "Wynn");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Travis Kauffman vs Amir Mansour";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-156"), Integer.parseInt("117"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-140"), Integer.parseInt("100"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-140"), Integer.parseInt("100"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Demetrius Andrade vs Jack Culcay";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-2000"), Integer.parseInt("900"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1600"), Integer.parseInt("800"), "Bovada");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1500"), Integer.parseInt("825"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1500"), Integer.parseInt("825"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "David Lemieux vs Curtis Stevens";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-286"), Integer.parseInt("203"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-400"), Integer.parseInt("300"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-390"), Integer.parseInt("300"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-390"), Integer.parseInt("300"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Artur Szpilka vs Dominic Breazeale";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-141"), Integer.parseInt("105"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-160"), Integer.parseInt("130"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-160"), Integer.parseInt("130"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-160"), Integer.parseInt("130"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Ohara Davies vs Derry Matthews";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-250"), Integer.parseInt("180"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-500"), Integer.parseInt("350"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-490"), Integer.parseInt("355"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-490"), Integer.parseInt("355"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Sam Eggington vs Paul Malignaggi";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-169"), Integer.parseInt("124"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-230"), Integer.parseInt("180"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-240"), Integer.parseInt("180"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-240"), Integer.parseInt("180"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Keith Thurman vs Danny Garcia";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-250"), Integer.parseInt("180"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-260"), Integer.parseInt("190"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-240"), Integer.parseInt("200"), "Westgate");
		events.add(e);
		e = new Event(key, Integer.parseInt("-235"), Integer.parseInt("200"), "BetOnline");
		events.add(e);
		e = new Event(key, Integer.parseInt("-245"), Integer.parseInt("195"), "MyBookie");
		events.add(e);
		e = new Event(key, Integer.parseInt("-240"), Integer.parseInt("225"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-255"), Integer.parseInt("215"), "BetNow");
		events.add(e);
		e = new Event(key, Integer.parseInt("-240"), Integer.parseInt("180"), "SportsBetting");
		events.add(e);
		e = new Event(key, Integer.parseInt("-245"), Integer.parseInt("225"), "SportBet");
		events.add(e);
		e = new Event(key, Integer.parseInt("-240"), Integer.parseInt("190"), "Station");
		events.add(e);
		e = new Event(key, Integer.parseInt("-230"), Integer.parseInt("180"), "Mirage");
		events.add(e);
		e = new Event(key, Integer.parseInt("-240"), Integer.parseInt("200"), "Wynn");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "David Avanesyan vs Lamont Peterson";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("226"), Integer.parseInt("-323"), "Opening");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Craig Kennedy vs Matty Askin";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("110"), Integer.parseInt("-147"), "Opening");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Jamie Conlan vs Yader Cardoza";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-10000"), Integer.parseInt("1600"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-5250"), Integer.parseInt("1750"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-5250"), Integer.parseInt("1750"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Cecilia Braekhus vs Klara Svensson";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-1000"), Integer.parseInt("620"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-2250"), Integer.parseInt("950"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1400"), Integer.parseInt("850"), "BetOnline");
		events.add(e);
		e = new Event(key, Integer.parseInt("-2250"), Integer.parseInt("1200"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-1400"), Integer.parseInt("850"), "SportsBetting");
		events.add(e);
		e = new Event(key, Integer.parseInt("-2250"), Integer.parseInt("1200"), "SportsBetting");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "James Dickens vs Thomas Patrick Ward";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-500"), Integer.parseInt("300"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-280"), Integer.parseInt("220"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-280"), Integer.parseInt("220"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-280"), Integer.parseInt("220"), "SportBet");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Chris Brown vs Soulja Boy";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("100"), Integer.parseInt("100"), "Opening");
		events.add(e);
		spoofBook.put(key, events);
		
		key = "Luke Campbell vs Jairo Lopez";
		events = new ArrayList<Event>();
		e = new Event(key, Integer.parseInt("-50000"), Integer.parseInt("1200"), "Opening");
		events.add(e);
		e = new Event(key, Integer.parseInt("-5000"), Integer.parseInt("1400"), "BOVADA.LV");
		events.add(e);
		e = new Event(key, Integer.parseInt("-5000"), Integer.parseInt("2000"), "5Dimes");
		events.add(e);
		e = new Event(key, Integer.parseInt("-5000"), Integer.parseInt("2000"), "SportBet");
		spoofBook.put(key, events);
		
		return spoofBook;
	}
}
