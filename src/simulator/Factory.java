package simulator;

import java.io.FileNotFoundException;
import java.io.IOException;

import simulator.Strategy.NullStrategy;
import simulator.Strategy.Strategy;
import simulator.Strategy.DumbStrategy;

public class Factory {

	private OrderBooks books = null;
	private TradeEngine te = null;

	//orders index number
	private int bidID	= 0;
	private int askID	= 0;
	private int tradeID = 0;


	public OrderBooks makeOrderBooks() {
		if (books == null) {
			books = new OrderBooks();
		}
		return books;
	}

	public TradeEngine makeTradeEngine() {
		if (te == null) {
			te = new TradeEngine(makeOrderBooks(), this);
		}
		return te;
	}

	public Strategy makeNullStrategy() {
		return new NullStrategy();
	}

	public Strategy makeDumbStrategy() {
		return new DumbStrategy(makeOrderBooks());
	}

	public Reader makeReader(String filepath) throws FileNotFoundException, IOException {
		return new Reader(filepath);
	}

	public Order makeOrder(String date, String time,
			String recordType, double price, double volume,
			String qualifiers, long   transactionID,
			long   bidID, long   askID,
			String bidAsk) {

		return new Order(date, time, recordType, price,
				volume, qualifiers, transactionID, bidID, askID, bidAsk);
	}

}
