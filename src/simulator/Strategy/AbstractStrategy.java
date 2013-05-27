package simulator.strategy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import simulator.Order;
import simulator.OrderBooks;


public abstract class AbstractStrategy implements Strategy {

	protected OrderBooks books;

	private BigInteger IDCounter = BigInteger.ONE.negate();// == -1


	protected List<Order> stratOrders = new ArrayList<Order>();
	
	private String previousOrderType;
	
	public AbstractStrategy(OrderBooks books) {
		this.books = books;
		this.previousOrderType = "";
	}

	private BigInteger generateStrategyID() {
		BigInteger returnInt = IDCounter;
		IDCounter = IDCounter.add(BigInteger.ONE.negate());// -= 1
		return returnInt;
	}

	@Override
	public Order submitOrder() {
		Order o = strategise();
		if (o != Order.NO_ORDER) {
			if ( !o.bidAsk().equals(previousOrderType) || previousOrderType.equals("") ) {
				addOrderToList(o);
				previousOrderType = new String( o.bidAsk() );
			}
		}
		addOrderToList(o);
		return o;
	}

	private void addOrderToList(Order o) {
		if (o != null) {
			stratOrders.add(0, o);
		}
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

	@Override
	public void reset() {
		IDCounter = BigInteger.ONE.negate();
		stratOrders = new ArrayList<Order>();
	}

}
