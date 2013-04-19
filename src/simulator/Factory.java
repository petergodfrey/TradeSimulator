package simulator;

import java.io.FileNotFoundException;
import java.io.IOException;

import simulator.Strategy.NullStrategy;
import simulator.Strategy.Strategy;
import simulator.Strategy.DumbStrategy;

public class Factory {
	
	private OrderBooks books = null;
	private TradeEngine te = null;
	
	public OrderBooks makeOrderBooks() {
		if (books == null) {
			books = new OrderBooks();
		}
		return books;
	}
	
	public TradeEngine makeTradeEngine() {
		if (te == null) {
			te = new TradeEngine(makeOrderBooks());
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

	

}
