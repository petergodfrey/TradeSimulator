package simulator;
public class Order {

    public final static Order NO_ORDER = null;
	
    
        
    private String instrument;
    private String date;
    private String time;
    private String recordType;
    private Double price;
    private Double volume;
    private String qualifiers;
    private long   transactionID;
    private long   bidID;
    private long   askID;
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
		
        this.instrument    = instrument;
        this.date          = date;
        this.time          = time;
        this.recordType    = recordType;
        this.price         = price;
        this.volume        = volume;
        this.qualifiers    = qualifiers;
        this.transactionID = transactionID;
        this.bidID         = bidID;
        this.askID         = askID;
        this.bidAsk        = bidAsk;
	}
    
    // Copy Constructor
    public Order(Order oldOrder) {
        this.instrument    = new String( oldOrder.instrument() );
        this.date          = new String( oldOrder.date() );
        this.time          = new String( oldOrder.time() );
        this.recordType    = new String( oldOrder.recordType() );
        this.price         = oldOrder.price();
        this.volume        = oldOrder.volume();
        this.qualifiers    = oldOrder.qualifiers();
        this.transactionID = oldOrder.transactionID();
        this.bidID         = oldOrder.bidID();
        this.askID         = oldOrder.askID();
        this.bidAsk        = oldOrder.bidAsk();
    }
    
    /*
     * Compares the timestamp with another trade and determines the earliest
     * 
     * Parameters:
     * t - The trade to compare with
     * 
     * Returns:
     * true if the trade has an earlier timestamp than the one given in t
     * false otherwise
     */
    public boolean isEarlier(Order bid) {
    	// TODO
    	return true;
    }
    
    
    /* Getter Methods */
    
    public String instrument() {
        return this.instrument;
    }
    
    public String date() {
        return this.date;
    }
    
    public String time() {
        return this.time;
    }
    
    public String recordType() {
        return this.recordType;
    }
    
    public Double price() {
        return this.price;
    }
        
    public Double volume() {
        return this.volume;
    }
    
    public String qualifiers() {
    	return this.qualifiers;
    }
    
    public long transactionID() {
        return this.transactionID;
    }
    
    public long bidID() {
        return this.bidID;
    }
    
    public long askID() {
        return this.askID;
    }
    
    public String toString() {
        return (instrument                   + ", " +
                date                         + ", " +
                time                         + ", " +
                recordType                   + ", " +
                Double.toString(price)       + ", " +
                Double.toString(volume)      + ", " +
                Long.toString(transactionID) + ", " +
                Long.toString(bidID)         + ", " +
                Long.toString(askID))        + ", " +
                bidAsk();
    }
    
    public boolean equals(Order t) {
        return ( this.instrument.equals(t.instrument)  &&
                 this.date.equals(t.date)              &&
                 this.time.equals(t.time)              &&
                 this.recordType.equals(t.recordType)  &&
                 this.price == t.price                 &&
                 this.volume == t.volume               &&
                 this.transactionID == t.transactionID &&
                 this.bidID == t.bidID                 &&
                 this.askID == t.askID                 &&
                 this.bidAsk == t.bidAsk);
    }

    

    
    // Change the volume field
    void updateVolume(double newVolume) {
    	this.volume = newVolume;
    }
    
    /* Getter Methods */
    
    public String bidAsk() {
        return this.bidAsk;
    }
    
    
    
}
