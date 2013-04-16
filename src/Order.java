public class Order extends Trade {

    public final static Order NO_ORDER = null;
	
    private String bidAsk;
        
	public Order(String security,
                 String date,
                 String time,
                 String recordType,
                 double price,
                 double volume,
                 double value,
                 long   transactionID,
                 long   bidID,
                 long   askID,
                 String bidAsk) {
        
        super(security, date, time, recordType,
              price, volume, value, transactionID,
              bidID, askID);
        
        this.bidAsk = bidAsk;
	}
    
    // Copy Constructor
    public Order(Order oldOrder) {
        super(oldOrder);
        this.bidAsk = new String( oldOrder.bidAsk() );
    }
    
    public String toString() {
        return ( super.toString().concat(bidAsk) );
    }
    
    /* Getter Methods */
    
    public String bidAsk() {
        return this.bidAsk;
    }
    
    public boolean equals(Order o) {
        return super.equals(o) && this.bidAsk.equals(o.bidAsk);
    }
    
}
