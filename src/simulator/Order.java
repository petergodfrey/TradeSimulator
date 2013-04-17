package simulator;
public class Order extends Trade {

    public final static Order NO_ORDER = null;
	
    private String bidAsk;
        
	public Order(String instrument,
                 String date,
                 String time,
                 String recordType,
                 double price,
                 double volume,
                 String qualifiers,
                 long   transactionID,
                 long   bidID,
                 long   askID,
                 String bidAsk) {
        
        super(instrument, date, time, recordType,
              price, volume, qualifiers, transactionID,
              bidID, askID);
        
        this.bidAsk = bidAsk;
	}
    
    // Copy Constructor
    public Order(Order oldOrder) {
        super(oldOrder);
        this.bidAsk = new String( oldOrder.bidAsk() );
    }
    
    public String toString() {
        return ( super.toString().concat(", " + bidAsk) );
    }
    
    // Change the volume field
    void updateVolume(double newVolume) {
    	this.volume = newVolume;
    }
    
    /* Getter Methods */
    
    public String bidAsk() {
        return this.bidAsk;
    }
    
    public boolean equals(Order o) {
        return super.equals(o) && this.bidAsk.equals(o.bidAsk);
    }
    
}
