public class Trade {
	
	/*
     * From the week 3 notes "How a trading engine works",
     * http://www.cse.unsw.edu.au/~fethir/TradingEngineCourse/Session2/Revised%20ContMatchingAlgorithm-1-5.htm
     * it is stated that the SIRCA format for representing ASX trades
     * has the following format:
     * Instrument, Date, Time, Record Type, Price, Volume, Value, Trans ID, Bid ID, Ask ID
     * The associated types of these data fields are also listed in the document.
     */
	protected String security;
    protected String date;
    protected String time;
    protected String recordType;
    protected Double price;
    protected Double volume;
    protected Double value;
    protected long   transactionID;
    protected long   bidID;
    protected long   askID;
        
	public Trade(String security,
                 String date,
                 String time,
                 String recordType,
                 double price,
                 double volume,
                 double value,
                 long   transactionID,
                 long   bidID,
                 long   askID) {
		
        this.security      = security;
        this.date          = date;
        this.time          = time;
        this.recordType    = recordType;
        this.price         = price;
        this.volume        = volume;
        this.value         = value;
        this.transactionID = transactionID;
        this.bidID         = bidID;
        this.askID         = askID;
	}
    
    // Copy Constructor
    public Trade(Trade oldTrade) {
        this.security      = new String( oldTrade.security() );
        this.date          = new String( oldTrade.date() );
        this.time          = new String( oldTrade.time() );
        this.recordType    = new String( oldTrade.recordType() );
        this.price         = oldTrade.price();
        this.volume        = oldTrade.volume();
        this.value         = oldTrade.value();
        this.transactionID = oldTrade.transactionID();
        this.bidID         = oldTrade.bidID();
        this.askID         = oldTrade.askID();
    }
    
    
    /* Getter Methods */
    
    public String security() {
        return this.security;
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
    
    public Double value() {
        return this.value;
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
        return (security                     + ", " +
                date                         + ", " +
                time                         + ", " +
                recordType                   + ", " +
                Double.toString(price)       + ", " +
                Double.toString(volume)      + ", " +
                Double.toString(value)       + ", " +
                Long.toString(transactionID) + ", " +
                Long.toString(bidID)         + ", " +
                Long.toString(askID));
    }
    
    public boolean equals(Trade t) {
        return ( this.security.equals(t.security)      &&
                 this.date.equals(t.date)              &&
                 this.time.equals(t.time)              &&
                 this.recordType.equals(t.recordType)  &&
                 this.price == t.price                  &&
                 this.volume == t.volume                &&
                 this.value == t.value                  &&
                 this.transactionID == t.transactionID &&
                 this.bidID == t.bidID                  &&
                 this.askID == t.askID );
    }
    
}
