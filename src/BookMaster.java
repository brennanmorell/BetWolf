import java.util.*;

public class BookMaster {
	private WebService service;
	private Map<String, List<Event>> book;
	
	public BookMaster(WebService w){
		service = w;
		book = new HashMap<String, List<Event>>();
	}
	
	public void fetchBooks(){
		book = service.fetch();
	}
	
	public void printBooks(){
		for(Map.Entry<String, List<Event>> entry : book.entrySet()){
			List<Event> value = entry.getValue();
			System.out.println(entry.getKey());
			for(Event eventOdds : value){
				System.out.println(eventOdds);
			}
			System.out.println();
		}
	}
}


