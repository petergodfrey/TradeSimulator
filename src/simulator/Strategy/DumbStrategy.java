package simulator.Strategy;

import simulator.Order;
import simulator.OrderBooks;

public class DumbStrategy implements Strategy {
    
    OrderBooks orderBooks;
    
    boolean ordered  = false;
    
    public Order order = null;
    
    public DumbStrategy(OrderBooks orderBooks) {
        this.orderBooks = orderBooks;
    }
    
    @Override
    public Order generateOrder() {

    	Order o = null;
    	if (orderBooks.bidListSize() != 0 && !ordered) {
    		Order bestbid = orderBooks.bestBidOrder();
    		 o = new Order(
    				bestbid.time(),
    				"ENTER", bestbid.price()-1, 1000, null, -1, "B");
    		 order = new Order(
     				bestbid.time(),
     				"ENTER", bestbid.price()-1, 1000, null, -1, "B");
    		 ordered = true;

    	}
        return o;
 //Order("CBA", "20130304", "00:00:00.000", "ENTER", 67.760, 15,new String(), new Long(0),new Long("6239925033924850752"), new Long(0),"B");
    }

	@Override
	public String getStrategyName() {
		return "DumbStrategy";
	}

	@Override
	public Order getOrderedOrders() {
		return order;
	}
    
}