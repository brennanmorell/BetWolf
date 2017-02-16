import java.util.*;

public class Wolf {
	public List<String> findOpportunities(Map<String, List<Spread>> book){
		List<String> arbitrages = new ArrayList<String>();
		for(String key : book.keySet()){
			List<Spread> spreads = book.get(key);
			for(Spread topSpread : spreads){
				for(Spread bottomSpread : spreads){
					computeArbitrage(topSpread, bottomSpread);
				}
			}
		}
		return arbitrages;
	}
	
	public void computeArbitrage(Spread topSpread, Spread bottomSpread){
		Double top = amerToDecimal(topSpread.getSide1());
		Double bottom = amerToDecimal(bottomSpread.getSide2());
		
		
		Double impliedTop= 1/top;
		Double impliedBottom = 1/bottom;
		
		Double eventSpace = impliedTop + impliedBottom;
		
		if(eventSpace < 1.0){
			Double amount1 = (100*impliedTop)/eventSpace;
			Double amount2 = (100*impliedBottom)/eventSpace;
			Double profit = (100/eventSpace)-100;
			System.out.println("--Arbitrage--");
			System.out.println("Top Odd: " + top + " (" + topSpread.getSide1() + ")" + " Bottom Odd: " + bottom + " (" + bottomSpread.getSide2()+")");
			System.out.println("Event: " + topSpread.getEvent());
			System.out.println("Top Bet: " + amount1 + " ("+topSpread.getSource()+")");
			System.out.println("Bottom Bet: " + amount2 + " (" + bottomSpread.getSource()+")");
			System.out.println("Profit: " + profit);
			System.out.println();
		}
	}
	
	public static Double amerToDecimal(int odd){
		if(odd > 0){
			return (odd/100.0)+1;
		}
		else{
			return (100.0/Math.abs(odd))+1;
		}
	}
}
