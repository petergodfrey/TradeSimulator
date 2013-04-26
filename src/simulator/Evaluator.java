package simulator;

import simulator.Strategy.Strategy;

public class Evaluator {
	
	private Strategy strat;
	private TradeEngine tradeEngine;

	public Evaluator(Strategy strat, TradeEngine tradeEngine) {
		this.strat = strat;
		this.tradeEngine = tradeEngine;
	}
	
	public void evaluate() {
		System.out.println("printing the profits of the trades");
		for (int i = 0; i < tradeEngine.getTradeList().size(); i++) {
			if (tradeEngine.getTradeList().get(i).ID() == -1) {
				System.out.println(tradeEngine.getTradeList().get(i).price() - strat.getOrderList().get(0).price());
			}
		}
	}

}
