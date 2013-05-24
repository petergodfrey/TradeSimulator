package simulator.strategy;

import java.util.List;

import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;
import simulator.Trade;
import simulator.TradeEngine;

public class MeanReversion extends AbstractStrategy implements Strategy {
	
	TradeEngine TE;
	
	private Double mean; 
	// if there's no my bid order, assigns false. otherwise, assigns true
	private boolean myBidOrder1 = true; //false;
	//private TradeEngine tradeEngine;
	
	private int tradeSize = 0;
	private double sum = 0;

	//private boolean myBidOrder = true;

	
	public MeanReversion(OrderBooks books, TradeEngine TE) {
		super(books);
		this.TE = TE;
	}
	
	public void calculateMean() {
			for (int i = 0; i < (TE.getTradeList().size()-tradeSize); i++) {
				sum += TE.getTradeList().get(i).price();
			}
		
		tradeSize = TE.getTradeList().size();
		this.mean = sum/TE.getTradeList().size();
		/*long sum = 0;
		List<Trade> tradeList = TE.getTradeList();
		for (Trade t:tradeList) {
			sum += t.price();
		}
		this.mean = (double) (sum / tradeList.size());
		//this.mean = TE.avg();*/
	}	
	
	@Override
	public Order strategise() {
		if (TE.getTradeList().size() > 0) {
			calculateMean();
		}
		Order order = null;
		// if the price is greater than mean, sell order
		if (books.bidListSize() != 0 && myBidOrder1 == true) {
			if (books.bestBidPrice() > this.mean ) { 
				// && if (you have stock to sell)
				Order bestBid = books.bestBidOrder();
				if (books.askListSize() != 0) {
					if (bestBid.price() < books.bestAskPrice()) {
						// volume should depend on my volume, so need to change later
						order = createOrder("ENTER", bestBid.price(), bestBid.volume(), null, "A");
						myBidOrder1 = false;
					} else {
						if (books.bestAskPrice() - 0.01 > this.mean) {
							// volume should depend on my volume, so need to change later
							order = createOrder("ENTER", books.bestAskPrice() - 0.01, bestBid.volume(), null, "A");
							myBidOrder1 = false;
						}
						// if bestAskPrice - 0.01 is lower than mean, don't sell anything
						// because you'll sell the stock with lower price than mean
					}
				} else {
					order = createOrder("ENTER", bestBid.price(), bestBid.volume(), null, "A");
					myBidOrder1 = false;
				}
			}
		}
		
		// if the price is lower than mean, buy order
		if (books.askListSize() != 0 && myBidOrder1 == false && order == null) {			
			Order bestAsk = books.bestAskOrder();
			if (bestAsk.price() < this.mean) { // check whether the price is lower than mean
				if (books.bidListSize() != 0) {
					if (books.bestBidPrice() < bestAsk.price()) {
						order = createOrder ("ENTER", bestAsk.price(), bestAsk.volume(), null, "B");
						myBidOrder1 = true;
					} else {
						if (books.bestBidPrice() + 0.01 <= this.mean) {
							order = createOrder ("ENTER", books.bestBidPrice() + 0.01, bestAsk.volume(), null, "B");
							myBidOrder1 = true;
						}
						// if bestBidPrice + 0.01 is greater than mean, don't buy anything
						// because you'll buy the stock with higher price than mean
					}
				} else {
					order = createOrder ("ENTER", bestAsk.price(), bestAsk.volume(), null, "B");
					myBidOrder1 = true;
				}
			}
		}
		
		return order;
	}

	@Override
	public String getStrategyName() {
		return "Mean Reversion";
	}
	
	@Override
	public void reset() {
		this.mean = 0.0;
	}

}
