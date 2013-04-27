package simulator;
import java.math.BigDecimal;
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
    private LinkedList<Order> bidList = new LinkedList<Order>();
    private Map<Long, Order> bidMap = new HashMap<Long, Order>();
    private LinkedList<Order> askList = new LinkedList<Order>();
    private Map<Long, Order> askMap = new HashMap<Long, Order>();
    
    private String simulatedTime = "";
    
    public String getSimulatedTime() {
    	return this.simulatedTime;
    }
    
    public static int convertTimeToMilliseconds(String time) {
    	String[] timeSplit = time.split(":");
    	BigDecimal milliseconds;
    	milliseconds =  new BigDecimal(timeSplit[2]).multiply(new BigDecimal("1000"));
    	milliseconds = milliseconds.add(new BigDecimal(timeSplit[1]).multiply(new BigDecimal("1000")).multiply(new BigDecimal("60")));
    	milliseconds = milliseconds.add(new BigDecimal(timeSplit[0]).multiply(new BigDecimal("1000")).multiply(new BigDecimal("60")).multiply(new BigDecimal("60")));
    	//milliseconds += Integer.parseInt(timeSplit[1]) * 1000 * 60;
    	//milliseconds += Integer.parseInt(timeSplit[0]) * 1000 * 60 * 60;
    	return milliseconds.intValue();
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
        return askList.getFirst();
    }
    // Returns the highest bid (buy) Order
    public Order bestBidOrder() {
        return bidList.getFirst();
    }
    // Returns the current spread
    public double spread() {
        return bestBidPrice() - bestAskPrice();
    }
    // Returns the lowest ask (sell) price
    public double bestAskPrice() {
        return askList.getFirst().price();
    }
    // Returns the highest bid (buy) price
    public double bestBidPrice() {
        return bidList.getFirst().price();
    }
    
    // Returns the size of the bid list
    public int bidListSize() {
        return bidList.size();
    }
    
    // Returns the size of the ask list
    public int askListSize() {
        return askList.size();
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
    public void deleteOrder(Order o) throws UnsupportedOperationException {
        if ( o.bidAsk().equals("B") ) {
        	bidList.remove( findByTransactionID(bidList, o.ID() ));
        } else if ( o.bidAsk().equals("A") ) {
        	askList.remove( findByTransactionID(askList, o.ID() ));
        } else {
        	throw new UnsupportedOperationException();
        }
    }
    
    
    /* Private Methods */


    
    private void enterOrder(Order o) throws UnsupportedOperationException {
    	if (o.bidAsk().equals("B")) {
    		insert(o, bidList);
    	}
    	else if (o.bidAsk().equals("A")) {
    		insert(o, askList);
    	} else {
    		throw new UnsupportedOperationException();
    	}
    }
    
	private void insert(Order o, LinkedList<Order> book) 
			throws UnsupportedOperationException {
		
		if (o.bidAsk().equals("B")) {
			for (int i = 0; i < book.size(); i++) {
				if (o.price() > book.get(i).price()) {
					book.add(i, o);
					return;
					//TODO sort on time when prices are same
					//TODO insert properly for sell book
					//TODO handle AMEND orders
				}
			}
			//catches orders that are either meant to be added to the end
			//of the list, or if the list was empty
			//and there was nothing to compare to
			book.addLast(o);
		}
		else if (o.bidAsk().equals("A"))  {
			for (int i = 0; i < book.size(); i++) {
				if (o.price() < book.get(i).price()) {
					book.add(i, o);
					return;
					//TODO sort on time when prices are same
					//TODO insert properly for sell book
					//TODO handle AMEND orders
				}
			}
			//catches orders that are either meant to be added to the end
			//of the list, or if the list was empty
			//and there was nothing to compare to
			book.addLast(o);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	private void amendOrder(Order o) {
		deleteOrder(o);
        if ( o.bidAsk().equals("B") ) {
        	insert(o, bidList);
        } else if ( o.bidAsk().equals("A") ) {
        	insert(o, askList);
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
    private Order findByTransactionID(List<Order> list, long transactionID) {
    	Order o = null;
    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).ID() == transactionID) {
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
				System.out.print("\t\t\t\t\t\t|");
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

