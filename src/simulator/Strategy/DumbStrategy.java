package simulator.Strategy;


import simulator.Order;
import simulator.OrderBooks;

public class DumbStrategy extends AbstractStrategy implements Strategy {

	boolean ordered  = false;

	public Order order = null;

	public DumbStrategy(OrderBooks books) {
		super(books);
	}

	@Override
	public Order strategise() {

		Order o = null;
		if (books.bidListSize() != 0 && !ordered) {
			Order bestbid = books.bestBidOrder();
			o = createOrder(
					"ENTER", bestbid.price()-1, 1000, null, "B");
			order = createOrder(
					"ENTER", bestbid.price()-1, 1000, null, "B");
			ordered = true;
		}
		return o;
	}

	@Override
	public String getStrategyName() {
		return "DumbStrategy";
	}



	


}