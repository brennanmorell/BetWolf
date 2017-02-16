
public class Spread {
	private String event;
	private String source;
	private int side1;
	private int side2;
	
	public Spread(String eStr, int s1, int s2, String src){
		event = eStr;
		side1 = s1;
		side2 = s2;
		source = src;
	}
	
	public String toString(){
		return side1+", "+side2+" " + "(" + source + ")";
	}
	
	public String getEvent(){
		return event;
	}
	
	public String getSource(){
		return source;
	}
	
	public int getSide1(){
		return side1;
	}
	
	public int getSide2(){
		return side2;
	}
}
