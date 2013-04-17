package simulator;
public class Trade {
	
	protected String instrument;
    protected String date;
    protected String time;
    protected String recordType;
    protected Double price;
    protected Double volume;
    protected String qualifiers;
    protected long   transactionID;
    protected long   bidID;
    protected long   askID;
        
	public Trade(String instrument,
                 String date,
                 String time,
                 String recordType,
                 double price,
                 double volume,
                 String qualifiers,
                 long   transactionID,
                 long   bidID,
                 long   askID) {
		
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
	}
    
    // Copy Constructor
    public Trade(Trade oldTrade) {
        this.instrument    = new String( oldTrade.instrument() );
        this.date          = new String( oldTrade.date() );
        this.time          = new String( oldTrade.time() );
        this.recordType    = new String( oldTrade.recordType() );
        this.price         = oldTrade.price();
        this.volume        = oldTrade.volume();
        this.qualifiers    = oldTrade.qualifiers();
        this.transactionID = oldTrade.transactionID();
        this.bidID         = oldTrade.bidID();
        this.askID         = oldTrade.askID();
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
    public boolean isEarlier(Trade t) {
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
                Long.toString(askID));
    }
    
    public boolean equals(Trade t) {
        return ( this.instrument.equals(t.instrument)  &&
                 this.date.equals(t.date)              &&
                 this.time.equals(t.time)              &&
                 this.recordType.equals(t.recordType)  &&
                 this.price == t.price                 &&
                 this.volume == t.volume               &&
                 this.transactionID == t.transactionID &&
                 this.bidID == t.bidID                 &&
                 this.askID == t.askID );
    }
    
}
