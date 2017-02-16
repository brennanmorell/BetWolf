import java.util.*;

public class BookMaster {
	private WebService service;
	private Wolf hunter;
	private Map<String, List<Spread>> book;
	
	public BookMaster(WebService w){
		service = w;
		hunter = new Wolf();
		book = new HashMap<String, List<Spread>>();
	}
	
	public void fetchBooks(){
		//book = service.fetch();
		book = service.spoofResults();
		hunter.findOpportunities(book);
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


