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
	private Map<Long, Order> bidMap = new HashMap<Long, Order>();
	private List<Order> askList = new ArrayList<Order>();
	private Map<Long, Order> askMap = new HashMap<Long, Order>();

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

	/*
	 * Inserts/processes a single order
	 * 
	 * Parameters:
	 * o - The order which is to be processed
	 */
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

	/*
	 * Delete an order from the order books
	 * 
	 * Paramaters:
	 * o - The DELETE order to be processed
	 * 
	 * Returns:
	 * true if a matching order was found and deleted from the order book
	 * false otherwise
	 */
	public Order deleteOrder(Order o) throws UnsupportedOperationException {
		Order toRemove = null;
		if ( o.bidAsk().equals("B") ) {
			toRemove =  findByID(bidList, o.ID());
			bidList.remove(toRemove);
		} else if ( o.bidAsk().equals("A") ) {
			toRemove = findByID(askList, o.ID());
			askList.remove(toRemove);
		} else {
			throw new UnsupportedOperationException();
		}
		return toRemove;
	}


	/* Private Methods */



	private void enterOrder(Order o) throws UnsupportedOperationException {
		if (o.bidAsk().equals("B")) {
			if (findByID(bidList, o.ID()) == null) {//TODO fix this
				insert(o, bidList);
			}
			
		}
		else if (o.bidAsk().equals("A")) {
			if (findByID(askList, o.ID()) == null) {
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
			removed = findByID(bidList, o.ID());
			if (removed != null) {
				int indexOfRemoved = bidList.indexOf(removed);
				if (removed != null && indexOfRemoved+1 < bidList.size() && o.price() <= bidList.get(indexOfRemoved+1).price() && o.volume() < removed.volume()) {
					//removed.updateVolume(o.volume());
					//removed.updatePrice(o.price());
					deleteOrder(removed);
					bidList.add(indexOfRemoved, o);
				} else {
					deleteOrder(removed);
					insert(o, bidList);
				}
			}
		} else if ( o.bidAsk().equals("A") ) {
			removed = findByID(askList, o.ID());
			if (removed != null) {
				int indexOfRemoved = askList.indexOf(removed);
				if (removed != null && indexOfRemoved+1 < askList.size() && o.price() <= askList.get(indexOfRemoved+1).price() && o.volume() < removed.volume()) {
					//removed.updateVolume(o.volume());
					//removed.updatePrice(o.price());
					deleteOrder(removed);
					askList.add(indexOfRemoved, o);
				} else {
					deleteOrder(removed);
					insert(o, askList);
				}
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/*
	 * Returns the index of the first occurrence of the element in the list which has
	 * a transaction ID equal to the given transaction ID
	 * 
	 * Parameters:
	 * list - The list to be searched
	 * transactionID - the transactionID of the order being searched for
	 * 
	 * Returns:
	 * The index of the first occurrence of the element with a matching transaction ID
	 * OR -1 if no matching order was found
	 */
	private Order findByID(List<Order> list, BigInteger transactionID) {
		Order o = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).ID().equals(transactionID)) {
				o = list.get(i);
			}
		}
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

