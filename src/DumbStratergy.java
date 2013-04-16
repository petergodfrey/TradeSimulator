public class DumbStratergy implements Stratergy {
    
    OrderBooks orderBooks;
    
    public DumbStratergy(OrderBooks orderBooks) {
        this.orderBooks = orderBooks;
    }
    
    public Order generateOrder() {
        return Order.NO_ORDER;
    }
}