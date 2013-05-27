package simulator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import simulator.strategy.*;

public class Evaluator {

	private Strategy strat;
	private TradeEngine tradeEngine;
	private OrderBooks books;

	
	public Evaluator(Strategy strat, TradeEngine tradeEngine, OrderBooks books) {
		this.strat = strat;
		this.tradeEngine = tradeEngine;
		this.books = books;
	}

	public List<Trade> evaluate() {
		List<Trade> strategyTrades = new ArrayList<Trade>();
		for (Trade t:tradeEngine.getTradeList()) {
			if (t.getAsk().ID().compareTo(BigInteger.ZERO) < 0 || t.getBid().ID().compareTo(BigInteger.ZERO) < 0) {
				strategyTrades.add(t);
			}
		}

		
		return strategyTrades;
	}
	
	public int calculateProfit(List<Trade> strategyTrades) {
		int profit = 0;
		int totalBuy = 0, totalSell = 0;
		for (Trade t:strategyTrades) {
			if (t.getAsk().ID().compareTo(BigInteger.ZERO) < 0) {
				profit += t.volume() * t.price();
				totalSell += t.volume() * t.price();
			}
			if (t.getBid().ID().compareTo(BigInteger.ZERO) < 0) {
				profit -= t.volume() * t.price();
				totalBuy += t.volume() * t.price();
			}
		}
		System.out.println("totalBuy");
		System.out.println(totalBuy);
		System.out.println("totalSell");
		System.out.println(totalSell);
		System.out.println("percentage");
		System.out.println(((double)(totalSell)/(double)(totalBuy)));
		return profit;
	}

}
