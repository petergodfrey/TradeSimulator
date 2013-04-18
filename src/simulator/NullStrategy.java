package simulator;

public class NullStrategy implements Strategy {

	@Override
	public Order generateOrder() {
		return null;
	}

	@Override
	public String getStrategyName() {
		return "Null Strategy";
	}

}