package simulator.Strategy;

import simulator.Order;

public class NullStrategy implements Strategy {

	@Override
	public Order generateOrder() {
		return null;
	}

	@Override
	public String getStrategyName() {
		return "Null Strategy";
	}

	@Override
	public Order getOrderedOrders() {
		// TODO Auto-generated method stub
		return null;
	}

}
