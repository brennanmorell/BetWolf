
public class Main {
	private static String bovadaBasketball = "http://sports.bovada.lv/";
	public static void main(String[] args){
		BovadaService b = new BovadaService(bovadaBasketball);
		
		BookMaster master = new BookMaster(b);
		master.fetchBooks();
		master.printBooks();
	}
}
