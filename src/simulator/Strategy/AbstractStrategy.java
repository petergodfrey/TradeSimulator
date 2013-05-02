package simulator.Strategy;

import java.util.ArrayList;
import java.util.List;

import simulator.Order;
import simulator.OrderBooks;


public abstract class AbstractStrategy implements Strategy {
	
	protected OrderBooks books;

	private long IDCounter = -1;
	
	private List<Order> stratOrders = new ArrayList<Order>();
	
	public AbstractStrategy(OrderBooks books) {
		this.books = books;
	}
	
	private Long generateStrategyID() {
		return IDCounter--;
	}
	
	@Override
	public Order submitOrder() {
		Order o = strategise();
		addOrderToList(o);
		return o;
	}
	
	private void addOrderToList(Order o) {
		stratOrders.add(o);
	}
	
	@Override
	public List<Order> getOrderList() {
		return this.stratOrders;
	}
	

	protected Order createOrder(
							String recordType,
							Double price,
							int volume,
							String qualifer,
							String bidAsk) {
		return new Order(books.getSimulatedTime(),recordType, price, volume, qualifer,
				generateStrategyID(), bidAsk);
	}

}
