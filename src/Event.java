
public class Event {
	private String eventString;
	private String source;
	private int side1;
	private int side2;
	
	public Event(String eStr, int s1, int s2, String src){
		eventString = eStr;
		side1 = s1;
		side2 = s2;
		source = src;
	}
	
	public String toString(){
		return side1+", "+side2+" " + "(" + source + ")";
	}
}
