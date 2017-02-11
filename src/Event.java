
public class Event {
	private String eventString;
	private String source;
	private String side1;
	private String side2;
	private int outcome1;
	private int outcome2;
	
	public Event(String eStr, String s1, String s2, int o1, int o2, String src){
		eventString = eStr;
		side1 = s1;
		side2 = s2;
		outcome1 = o1;
		outcome2 = o2;
		source = src;
	}
	
	public String toString(){
		return side1+": " + outcome1 + ", " + side2 + ": " + outcome2;
	}
}
