import java.util.*;

public class Wolf {
	private AlertService alert_service;
	
	public Wolf(AlertService a){
		alert_service = a;
	}
	
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
		Double top = amerToDecimal(topSpread.getSide(0));
		Double bottom = amerToDecimal(bottomSpread.getSide(1));
		
		
		Double impliedTop= 1/top;
		Double impliedBottom = 1/bottom;
		
		Double eventSpace = impliedTop + impliedBottom;
		
		if(eventSpace < 1.0){
			Double amount1 = (1*impliedTop)/eventSpace;
			Double amount2 = (1*impliedBottom)/eventSpace;
			Double profit = (1/eventSpace)-1;
			String arbStr = generateArbStr(top, bottom, topSpread, bottomSpread, amount1, amount2, profit);
			System.out.println(arbStr);
			alert_service.sendEmail(arbStr);
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
	
	public static String generateArbStr(Double top, Double bottom, Spread topSpread, Spread bottomSpread, Double amount1, Double amount2, Double profit){
		String s = "--Arbitrage--\n";
		s+="Top Odd: " + top + " (" + topSpread.getSide(0) + ")" + " Bottom Odd: " + bottom + " (" + bottomSpread.getSide(1)+")\n";
		s+="Event: " + topSpread.getEvent()+"\n";
		s+="Top Bet: " + amount1 + " ("+topSpread.getSource()+")\n";
		s+="Bottom Bet: " + amount2 + " (" + bottomSpread.getSource()+")\n";
		s+="Profit: " + profit+"\n";
		/*System.out.println("--Arbitrage--");
		System.out.println("Top Odd: " + top + " (" + topSpread.getSide(0) + ")" + " Bottom Odd: " + bottom + " (" + bottomSpread.getSide(1)+")");
		System.out.println("Event: " + topSpread.getEvent());
		System.out.println("Top Bet: " + amount1 + " ("+topSpread.getSource()+")");
		System.out.println("Bottom Bet: " + amount2 + " (" + bottomSpread.getSource()+")");
		System.out.println("Profit: " + profit);
		System.out.println();*/
		return s;
	}
}
