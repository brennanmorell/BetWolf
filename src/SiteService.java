import java.util.*;

public interface SiteService {
	public Map<String,Event> fetchDocument();
	public void parseDocument(String bookString);
	public void printDocument();
			
}
