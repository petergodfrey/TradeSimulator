package simulator.strategy;

import java.util.List;

import simulator.Order;

public interface Strategy {

    
    public Order submitOrder();
    
    public Order strategise();
    
    public String getStrategyName();
    //name is used for display in UI
    
    public List<Order> getOrderList();

	//public Order createOrder(String time, String recordType, Double price,
		//	int volume,	String qualifer, String bidAsk);
    

    
    
    
}