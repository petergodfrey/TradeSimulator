package simulator;

import java.util.*;
import java.lang.Math;

public class TradeEngine {

	private OrderBooks       orderBooks;
	private ArrayList<Trade> tradeList;
	private Factory f;
	private String openTrading = "10:00:00.000";

	private long sum;

	public TradeEngine(OrderBooks orderBooks, Factory f) {
		this.orderBooks = orderBooks;
		this.tradeList = new ArrayList<Trade>();
		this.f = f;
	}

	private boolean timeToTrade(String currentTime, String openingTime) {
		return (OrderBooks.convertTimeToMilliseconds(currentTime) >= 
				OrderBooks.convertTimeToMilliseconds(openingTime));
	}

	public boolean setOpenTradingTime(String newOpening) {
		try {
			OrderBooks.convertTimeToMilliseconds(newOpening);
		} catch (Exception e) {
			return false;
		}
		this.openTrading = newOpening;
		return true;
	}

	public void resetTradeEngine() {
		tradeList = new ArrayList<Trade>();
		sum = 0;
	}

	public void trade() {
		if (!timeToTrade(orderBooks.getSimulatedTime(), openTrading)) {

			//if not open trading time, don't trade
			return;
		}

		//Generate a trade transaction with the following properties:
		//    i.  Volume traded is the minimum volume of best bid and best ask orders.
		//    ii. The trading price is determined as:

		if (orderBooks.spread() < 0) {
			//if it is open trading session, but spread is < 0, can't trade
			return;
		}
		//there is a trade available now, falls into 2 categories
		//equal volume or not
		//if equal volume, make trade and both bid and ask from orderbook
		//if not, update volume of one and remove the other
		//then recurse to see if there is another trade available
		Order bestBid = orderBooks.bestBidOrder();
		Order bestAsk = orderBooks.bestAskOrder();

		if (bestBid.volume() > bestAsk.volume()) {
			bestBid.updateVolume(bestBid.volume() - bestAsk.volume());
			orderBooks.deleteOrder(bestAsk);
			addTrade(bestBid, bestAsk);
			trade();

		}
		else if (bestBid.volume() < bestAsk.volume()) {
			bestAsk.updateVolume(bestAsk.volume() - bestBid.volume());
			orderBooks.deleteOrder(bestBid);
			addTrade(bestBid, bestAsk);
			trade();

		} else {
			orderBooks.deleteOrder(bestBid);
			orderBooks.deleteOrder(bestAsk);
			addTrade(bestBid, bestAsk);
		}

		return;

	}

	public ArrayList<Trade> getTradeList () {
		return tradeList;
	}

	private void addTrade(Order bid, Order ask) {

		double tradePrice = ask.price();//always trades at ask price

		int volume = Math.min(bid.volume(), ask.volume());

		sum += tradePrice;

		tradeList.add(0, f.makeTrade(orderBooks.getSimulatedTime(),
				"TRADE",
				tradePrice,
				volume,
				"",            // TODO Qualifiers
				"", bid, ask) );
	}

	public double avg() {
		return (double)(sum/tradeList.size());
	}
	
	public void displayTradeList() {
		for (Trade t:tradeList) {
			displayTrade(t);
		}
	}
	
	private static void displayTrade(Trade t) {

		System.out.print(t.ID() + "\t");
		System.out.print(String.format("%019d", t.getBid().ID())+"\t");
		System.out.print(String.format("%019d", t.getAsk().ID())+"\t");
		System.out.print(t.price()+"\t");
		System.out.print(t.volume());

		System.out.println();
	}

}