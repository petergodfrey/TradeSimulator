package simulator.Strategy;

import java.util.ArrayList;
import java.util.List;

import simulator.Order;


public abstract class AbstractStrategy implements Strategy {

	private long IDCounter = -1;
	
	private List<Order> stratOrders = new ArrayList<Order>();
	
	private Long generateStrategyID() {
		return IDCounter--;
	}
	
	@Override
	public Order submitOrder() {
		Order o = strategise();
		addOrderToList(o);
		return o;
	}
	
	protected void addOrderToList(Order o) {
		stratOrders.add(o);
	}
	
	@Override
	public List<Order> getOrderList() {
		return this.stratOrders;
	}
	

	public Order createOrder(String time,
							String recordType,
							Double price,
							int volume,
							String qualifer,
							String bidAsk) {
		return new Order(time,recordType, price, volume, qualifer,
				generateStrategyID(), bidAsk);
	}

}
