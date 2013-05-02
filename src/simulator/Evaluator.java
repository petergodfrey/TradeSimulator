package simulator;

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
			if (t.getAsk().ID() < 0 || t.getBid().ID() < 0) {
				strategyTrades.add(t);
			}
		}

		
		return strategyTrades;
	}


	public int calculateProfit(List<Trade> strategyTrades) {
		int profit = 0;

		for (Trade t:strategyTrades) {
			if (t.getAsk().ID() < 0) {
				profit += t.getAsk().volume() * t.getAsk().price();
			}
			if (t.getBid().ID() < 0) {
				profit -= t.getBid().volume() * t.getBid().price();
			}
		}
		return profit;
	}

}
