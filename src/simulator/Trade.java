package simulator;

public class Trade extends Order {
	
	private final Order bid;
	private final Order ask;

	public Trade(
			String time,
			String recordType,
			double price,
			int volume,
			String qualifiers,
			long ID,
			String bidAsk,
			Order bid,
			Order ask) {
		super(time, recordType, price, volume, qualifiers, ID, bidAsk);
		this.bid = bid;
		this.ask = ask;
	}
	
	public Order getBid() {
		return this.bid;
	}
	
	public Order getAsk() {
		return this.ask;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", " + bid.ID() + ", " + ask.ID();
		
	}

}
