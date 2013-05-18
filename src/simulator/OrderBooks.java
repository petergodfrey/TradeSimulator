package simulator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/* This class manages the order books for the simulator.
 * The order books consist of two lists of orders, one for
 * Ask orders and one for Bid orders.
 * All orders are for a single instrument.
 */

public class OrderBooks {

	/*
	 * The bidList and askList will both be ordered by price/time
	 */
	private List<Order> bidList = new ArrayList<Order>();
	private Map<BigInteger, Order> bidMap = new HashMap<BigInteger, Order>();
	private List<Order> askList = new ArrayList<Order>();
	private Map<BigInteger, Order> askMap = new HashMap<BigInteger, Order>();

	private String simulatedTime = "";

	public String getSimulatedTime() {
		return this.simulatedTime;
	}

	public static boolean compareDoubleEquals(Double d1, Double d2) {
		return ((d2 - d1) > -0.0001 && (d1 - d2) < 0.0001);
	}

	public static int convertTimeToMilliseconds(String time) {
		String[] timeSplit = time.split(":");
		BigDecimal milliseconds;
		milliseconds =  new BigDecimal(timeSplit[2]).multiply(new BigDecimal("1000"));
		milliseconds = milliseconds.add(new BigDecimal(timeSplit[1]).multiply(new BigDecimal("1000")).multiply(new BigDecimal("60")));
		milliseconds = milliseconds.add(new BigDecimal(timeSplit[0]).multiply(new BigDecimal("1000")).multiply(new BigDecimal("60")).multiply(new BigDecimal("60")));
		return milliseconds.intValue();
	}

	public void resetOrderBooks() {
		bidList = new LinkedList<Order>();
		askList = new LinkedList<Order>();
	}

	public void processOrder(Order o) throws UnsupportedOperationException {

		if ( o.recordType().equals("ENTER") ) {
			enterOrder(o);
		} else if ( o.recordType().equals("DELETE") ) {
			deleteOrder(o);
		} else if ( o.recordType().equals("AMEND") ) {
			amendOrder(o);
		} else {
			// Record Type is unsupported, Sirca data should never reach here
			throw new UnsupportedOperationException();
		}
		//update the simulated time at each order given
		simulatedTime = o.time();
	}

	// Returns the lowest ask (sell) Order
	public Order bestAskOrder() {
		return askList.get(0);
	}
	// Returns the highest bid (buy) Order
	public Order bestBidOrder() {
		return bidList.get(0);
	}
	// Returns the current spread
	public double spread() {
		try {
			return bestBidPrice() - bestAskPrice();
		} catch (Exception e) {
			//if any sort of error in retrieving spread,
			//make it negative and unable to trade
			return Integer.MIN_VALUE;
		}
	}
	// Returns the lowest ask (sell) price
	public double bestAskPrice() {
		return bestAskOrder().price();
	}
	// Returns the highest bid (buy) price
	public double bestBidPrice() {
		return bestBidOrder().price();
	}

	// Returns the size of the bid list
	public int bidListSize() {
		return bidList.size();
	}

	// Returns the size of the ask list
	public int askListSize() {
		return askList.size();
	}
	
	public Map<BigInteger, Order> bidMap () {
		return this.bidMap;
	}
	
	public Map<BigInteger, Order> askMap () {
		return this.askMap;
	}

	// Returns a deep copy of the askList
	public LinkedList<Order> askList() {
		LinkedList<Order> clone = new LinkedList<Order>();
		for (Order order: askList) { 
			clone.add( new Order(order) );
		}
		return clone;
	}

	// Returns a deep copy of the bidList
	public LinkedList<Order> bidList() {
		LinkedList<Order> clone = new LinkedList<Order>();
		for (Order order: bidList) { 
			clone.add( new Order(order) );
		}
		return clone;
	}

	public Order deleteOrder(Order o) throws UnsupportedOperationException {
		Order toRemove = null;
		if ( o.bidAsk().equals("B") ) {
			toRemove =  findByID(bidMap, o.ID());
			bidList.remove(toRemove);
			bidMap.remove(o.ID());
		} else if ( o.bidAsk().equals("A") ) {
			toRemove = findByID(askMap, o.ID());
			askList.remove(toRemove);
			askMap.remove(o.ID());
		} else {
			throw new UnsupportedOperationException();
		}
		return toRemove;
	}

	private void enterOrder(Order o) throws UnsupportedOperationException {
		if (o.bidAsk().equals("B")) {
			if (findByID(bidMap, o.ID()) == null) {//TODO fix this
				bidMap.put(o.ID(), o);
				insert(o, bidList);
			}
			
		}
		else if (o.bidAsk().equals("A")) {
			if (findByID(askMap, o.ID()) == null) {
				askMap.put(o.ID(), o);
				insert(o, askList);
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}

	private void insert(Order o, List<Order> book) 
			throws UnsupportedOperationException {

		if (o.bidAsk().equals("B")) {
			for (int i = 0; i < book.size(); i++) {
				if (o.price() > book.get(i).price()) {
					book.add(i, o);
					return;
					//TODO sort on time when prices are same
					//TODO insert properly for sell book
				}
			}
			//catches orders that are either meant to be added to the end
			//of the list, or if the list was empty
			//and there was nothing to compare to
			book.add(book.size(), o);
		}
		else if (o.bidAsk().equals("A"))  {
			for (int i = 0; i < book.size(); i++) {
				if (o.price() < book.get(i).price()) {
					book.add(i, o);
					return;
					//TODO sort on time when prices are same
					//TODO insert properly for sell book
				}
			}
			//catches orders that are either meant to be added to the end
			//of the list, or if the list was empty
			//and there was nothing to compare to
			book.add(book.size(), o);
		} else {
			throw new UnsupportedOperationException();
		}

	}

	private void amendOrder(Order o) {
		Order removed;
		if ( o.bidAsk().equals("B") ) {
			removed = findByID(bidMap, o.ID());
			if (removed != null) {
				int indexOfRemoved = bidList.indexOf(removed);
				if (removed != null && indexOfRemoved+1 < bidList.size() && o.price() <= bidList.get(indexOfRemoved+1).price() && o.volume() < removed.volume()) {
					deleteOrder(removed);
					bidList.add(indexOfRemoved, o);
					bidMap.put(o.ID(), o);
				} else {
					deleteOrder(removed);
					insert(o, bidList);
					bidMap.put(o.ID(), o);
				}
			}
		} else if ( o.bidAsk().equals("A") ) {
			removed = findByID(askMap, o.ID());
			if (removed != null) {
				int indexOfRemoved = askList.indexOf(removed);
				if (removed != null && indexOfRemoved+1 < askList.size() && o.price() <= askList.get(indexOfRemoved+1).price() && o.volume() < removed.volume()) {
					deleteOrder(removed);
					askList.add(indexOfRemoved, o);
					askMap.put(o.ID(), o);
				} else {
					deleteOrder(removed);
					insert(o, askList);
					askMap.put(o.ID(), o);
				}
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}


	private Order findByID(Map<BigInteger, Order> map, BigInteger ID) {
		Order o = null;
		o = map.get(ID);
		return o;
	}

	public void display() {
		//text display of order books for debugging
		clearConsole();
		System.out.println("bid\t\t\t\t\t|Ask");
		for (int i = 0; i < bidList.size() || i < askList.size(); i++) {
			if (i < bidList.size()) {
				System.out.print(bidList.get(i).ID()+"\t");
				System.out.print(bidList.get(i).price()+"\t");
				System.out.print(bidList.get(i).volume() + "\t|");
			} else {
				System.out.print("\t\t\t\t\t|");
			}
			if (i < askList.size()) {
				System.out.print(askList.get(i).ID()+"\t");
				System.out.print(askList.get(i).price()+"\t");
				System.out.print(askList.get(i).volume());
			}
			System.out.println();
		}
	}

	public static void clearConsole() {
		//works only in command line
		String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}

}

