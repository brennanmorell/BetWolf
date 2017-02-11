import java.util.*;

public class BookMaster {
	private BovadaService bovadaService;
	private Map<String, Event> bovadaBasketball;
	public BookMaster(BovadaService bService){
		bovadaService = bService;
		bovadaBasketball = new HashMap<String, Event>();
	}
	
	public void fetchBooks(){
		bovadaBasketball = bovadaService.fetchDocument();
	}
	
	public void printBooks(){
		System.out.println("Bovada Basketball");
		for (Map.Entry<String, Event> entry : bovadaBasketball.entrySet()) {
		    Event value = entry.getValue();
		    System.out.println(value.toString());
		}
	}
}
