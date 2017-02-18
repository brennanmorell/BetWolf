
public class CrossBook {
	private static String NBA_BOOK_STR = "nba";
	private static String NHL_BOOK_STR = "nhl";
	private static String UFC_BOOK_STR = "ufc";
	private static String NCAAB_BOOK_STR = "ncaab";
	private static String BOXING_BOOK_STR = "boxing";
	private static String ATP_BOOK_STR = "atp";
	
	private static String BASKETBALL = "basketball";
	private static String HOCKEY = "hockey";
	private static String FIGHTING = "fighting";
	private static String TENNIS = "tennis";
	
	private static int TWO_SIDED = 2;
	private static int THREE_SIDED = 3;
	
	public static void main(String[] args){
		WebService nbaService = new WebService(NBA_BOOK_STR, BASKETBALL);
		WebService nhlService = new WebService(NHL_BOOK_STR, HOCKEY);
		WebService ufcService = new WebService(UFC_BOOK_STR, FIGHTING);
		WebService ncaabService = new WebService(NCAAB_BOOK_STR, BASKETBALL);
		WebService boxingService = new WebService(BOXING_BOOK_STR, FIGHTING);
		WebService atpService = new WebService(ATP_BOOK_STR, TENNIS);
		
		BookMaster nbaMaster = new BookMaster(nbaService, TWO_SIDED);
		BookMaster nhlMaster = new BookMaster(nhlService, TWO_SIDED);
		BookMaster ufcMaster = new BookMaster(ufcService, TWO_SIDED);
		BookMaster ncaabMaster = new BookMaster(ncaabService, TWO_SIDED);
		BookMaster boxingMaster = new BookMaster(boxingService, TWO_SIDED);
		BookMaster atpMaster = new BookMaster(atpService, TWO_SIDED);
		
		System.out.println("NBA");
		System.out.println();
		nbaMaster.fetchBooks();
		//nbaMaster.printBooks();
		System.out.println();
		
		System.out.println("NHL");
		System.out.println();
		nhlMaster.fetchBooks();
		//nhlMaster.printBooks();
		System.out.println();
		
		System.out.println("UFC");
		System.out.println();
		ufcMaster.fetchBooks();
		//ufcMaster.printBooks();
		System.out.println();
		
		System.out.println("NCAAB");
		System.out.println();
		ncaabMaster.fetchBooks();
		//ncaabMaster.printBooks();
		System.out.println();
		
		System.out.println("BOXING");
		System.out.println();
		boxingMaster.fetchBooks();
		//boxingMaster.printBooks();
		System.out.println();
		
		System.out.println("ATP");
		System.out.println();
		atpMaster.fetchBooks();
		//atpMaster.printBooks();
		System.out.println();
	}
}
