import java.util.*;

public class BookMaster {
	private WebService service;
	private Wolf wolf;
	private Map<String, List<Spread>> book;
	private int outcomes;
	
	public BookMaster(WebService w, int o){
		service = w;
		wolf = new Wolf(new AlertService());
		book = new HashMap<String, List<Spread>>();
		outcomes = o;
	}
	
	public void fetchBooks(){
		book = service.fetch(outcomes);
		wolf.findOpportunities(book);
	}
	
	public void printBooks(){
		for(Map.Entry<String, List<Spread>> entry : book.entrySet()){
			List<Spread> value = entry.getValue();
			System.out.println(entry.getKey());
			for(Spread eventOdds : value){
				System.out.println(eventOdds);
			}
			System.out.println();
		}
	}
}


