package simulator.strategy;

import simulator.Order;
import simulator.OrderBooks;
import simulator.TradeEngine;

public class NewStrategy extends AbstractStrategy implements Strategy {
	
	private final TradeEngine TE;

	public NewStrategy(OrderBooks books, TradeEngine TE) {
		super(books);
		this.TE = TE;
	}

	@Override
	public String getStrategyName() {
		// TODO Auto-generated method stub
		return "New Strategy";
	}

	@Override
	public Order strategise() {
		// TODO Auto-generated method stub
		return null;
	}

}
