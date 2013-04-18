package simulator;
public class DumbStrategy implements Strategy {
    
    OrderBooks orderBooks;
    
    public DumbStrategy(OrderBooks orderBooks) {
        this.orderBooks = orderBooks;
    }
    
    @Override
    public Order generateOrder() {
        return Order.NO_ORDER;
    }

	@Override
	public String getStrategyName() {
		return "DumbStrategy";
	}
    
    
}