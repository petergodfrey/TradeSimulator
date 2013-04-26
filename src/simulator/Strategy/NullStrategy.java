package simulator.strategy;

import simulator.Order;

public class NullStrategy extends AbstractStrategy implements Strategy {

	@Override
	public String getStrategyName() {
		return "Null Strategy";
	}

	@Override
	public Order strategise() {
		// TODO Auto-generated method stub
		return null;
	}



}
