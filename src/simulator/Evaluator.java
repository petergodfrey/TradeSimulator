package simulator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import simulator.gui.Main;
import simulator.strategy.*;

public class Evaluator {

	private Strategy strat;
	private TradeEngine tradeEngine;
	private OrderBooks books;
	
	double profit = 0;
	int totalBuy = 0;
	int totalSell = 0;

	
	public Evaluator(Strategy strat, TradeEngine tradeEngine, OrderBooks books) {
		this.strat = strat;
		this.tradeEngine = tradeEngine;
		this.books = books;
		evaluate();
	}

	public List<Trade> filterStrategyTrades() {
		List<Trade> strategyTrades = new ArrayList<Trade>();
		for (Trade t:tradeEngine.getTradeList()) {
			if (t.getAsk().ID().compareTo(BigInteger.ZERO) < 0 || t.getBid().ID().compareTo(BigInteger.ZERO) < 0) {
				strategyTrades.add(t);
			}
		}

		return strategyTrades;
	}
	
	private void evaluate() {
		for (Trade t:filterStrategyTrades()) {
			if (t.getAsk().ID().compareTo(BigInteger.ZERO) < 0) {
				this.profit += t.volume() * t.price();
				this.totalSell += t.volume() * t.price();
			}
			if (t.getBid().ID().compareTo(BigInteger.ZERO) < 0) {
				this.profit -= t.volume() * t.price();
				this.totalBuy += t.volume() * t.price();
			}
		}
	}
	
	public Integer getTotalBuy() {
		return this.totalBuy;
	}
	
	public Integer getTotalSell() {

		return this.totalSell;
	}
	
	public Double getProfit() {
		return this.profit;
	}

}
