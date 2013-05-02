package simulator.strategy;

import simulator.Order;
import simulator.OrderBooks;

public class NullStrategy extends AbstractStrategy implements Strategy {

	public NullStrategy(OrderBooks books) {
		super(books);
	}

	@Override
	public String getStrategyName() {
		return "Null Strategy";
	}

	@Override
	public Order strategise() {
		return null;
	}




}
