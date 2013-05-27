package simulator.strategy;

import java.util.List;

import simulator.Order;

public interface Strategy {

    
    public Order submitOrder();
    
    public Order strategise();
    
    public String getStrategyName();
    //name is used for display in UI
    
    public List<Order> getOrderList();
    
    public void reset();
}