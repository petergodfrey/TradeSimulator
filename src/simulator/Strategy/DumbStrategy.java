package simulator.Strategy;


import simulator.Order;
import simulator.OrderBooks;

public class DumbStrategy extends AbstractStrategy implements Strategy {

	OrderBooks orderBooks;

	boolean ordered  = false;

	public Order order = null;

	public DumbStrategy(OrderBooks orderBooks) {
		this.orderBooks = orderBooks;
	}

	@Override
	public Order strategise() {

		Order o = null;
		if (orderBooks.bidListSize() != 0 && !ordered) {
			Order bestbid = orderBooks.bestBidOrder();
			o = createOrder(
					bestbid.time(), "ENTER", bestbid.price()-1, 1000, null, "B");
			order = createOrder(
					bestbid.time(), "ENTER", bestbid.price()-1, 1000, null, "B");
			ordered = true;
		}
		return o;
	}

	@Override
	public String getStrategyName() {
		return "DumbStrategy";
	}



	


}