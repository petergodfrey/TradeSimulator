package simulator;
interface Strategy {

    public Order generateOrder();
    
    public String getStrategyName();
    //name is used for display in UI
    
    public Order getOrderedOrders();
    
    
    
}