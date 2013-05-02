package simulator;
public class Order {

	public final static Order NO_ORDER = null;

	private String 			time;
	private final String 	recordType;
	private Double 			price;
	private int 			volume;
	private String 			qualifiers;
	private long   			ID;
	private final String 	bidAsk;

	public Order(
			String time,
			String recordType,
			double price,
			int volume,
			String qualifiers,
			long   ID,
			String bidAsk) {

		//this.instrument    = instrument;
		this.time          = time;
		this.recordType    = recordType;
		this.price         = price;
		this.volume        = volume;
		this.qualifiers    = qualifiers;
		this.ID     	   = ID;
		this.bidAsk        = bidAsk;
	}
	
	// Copy constructor
	public Order(Order o) {
		this.time          = o.time;
		this.recordType    = o.recordType;
		this.price         = o.price;
		this.volume        = o.volume;
		this.qualifiers    = o.qualifiers;
		this.ID     	   = o.ID;
		this.bidAsk        = o.bidAsk;
	}
	    

	/* Getter Methods */

	public String time() {
		return this.time;
	}

	public String recordType() {
		return this.recordType;
	}

	public Double price() {
		return this.price;
	}

	public int volume() {
		return this.volume;
	}

	public String qualifiers() {
		return this.qualifiers;
	}

	public long ID() {
		return this.ID;
	}

	@Override
	public String toString() {
		return (		time                         + ", " +
						recordType                   + ", " +
						Double.toString(price)       + ", " +
						Double.toString(volume)      + ", " +
						Long.toString(ID) 			 + ", " +
						bidAsk());
	}

	public boolean equals(Order t) {
		return (this.time.equals(t.time)              &&
				this.recordType.equals(t.recordType)  &&
				this.price == t.price                 &&
				this.volume == t.volume               &&
				this.ID == t.ID 					  &&
				this.bidAsk == t.bidAsk);
	}

	// Change the volume field
	void updateVolume(int newVolume) {
		this.volume = newVolume;
	}

	/* Getter Methods */

	public String bidAsk() {
		return this.bidAsk;
	}



}
