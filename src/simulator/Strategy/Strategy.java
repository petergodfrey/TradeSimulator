package simulator.Strategy;

import simulator.Order;

public interface Strategy {

    public Order generateOrder();
    
    public String getStrategyName();
    //name is used for display in UI
    
    public Order getOrderedOrders();
    
    
    
}