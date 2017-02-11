import java.util.*;

public interface WebService {
	public Map<String,Event> fetchDocument();
	public void parseDocument(String bookString);
	public void printDocument();
			
}
