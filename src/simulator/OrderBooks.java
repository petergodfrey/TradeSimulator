package simulator;
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
    LinkedList<Order> bidList = new LinkedList<Order>();
    LinkedList<Order> askList = new LinkedList<Order>();
    
    /*
     * Inserts/processes a single order
     * 
     * Parameters:
     * o - The order which is to be processed
     */
    public void processOrder(Order o) throws UnsupportedOperationException {
    	//try {
    	//	System.out.println(spread());
    	//} catch (Exception e) {
    		
    	//}
    	//System.out.println(bidList.size() + ", " + askList.size());
    	if ( o.recordType().equals("ENTER") ) {
        	enterOrder(o);
        } else if ( o.recordType().equals("DELETE") ) {
    		deleteOrder(o);
        } else if ( o.recordType().equals("AMEND") ) {
    		amendOrder(o);
        } else if ( o.recordType().equals("TRADE") ) {
        	//should never reach here delete when done
    		trade(o);
        } else if ( o.recordType().equals("OFFTR") ) {
        	//should never reach here delete when done
        	offTrade(o);
        } else if ( o.recordType().equals("CANCEL_TRADE") ) {
        	//should never reach here delete when done
        	cancelTrade(o);
    	} else {
    		// Record Type is unsupported, Sirca data should never reach here
    		throw new UnsupportedOperationException();
    	}
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
        return bestBidPrice() - bestAskPrice();
    }
    // Returns the lowest ask (sell) price
    public double bestAskPrice() {
        return askList.get(0).price();
    }
    // Returns the highest bid (buy) price
    public double bestBidPrice() {
        return bidList.get(0).price();
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
    boolean deleteOrder(Order o) {
        if ( o.bidAsk().equals("B") ) {
        	bidList.remove( findByTransactionID( bidList, o.transactionID() ) );
        	return true;
        } else if ( o.bidAsk().equals("A") ) {
        	askList.remove( findByTransactionID( askList, o.transactionID() ) );
        	return true;
        } else {
            return false;
        }
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
    boolean updateVolume(Order o, double newVolume) {
        if ( o.bidAsk().equals("B") ) {
        	bidList.remove( findByTransactionID( bidList, o.transactionID() ) );
        	return true;
        } else if ( o.bidAsk().equals("A") ) {
        	askList.remove( findByTransactionID( askList, o.transactionID() ) );
        	return true;
        } else {
            return false;
        }
    }
    
    /* Private Methods */

    // Takes a List of Orders and returns a deep copy
    private ArrayList<Order> copyList(List<Order> list) {
        ArrayList<Order> copy = new ArrayList<Order>();
        for (Order o : list) {
            copy.add( new Order(o) ); // Add a copy of the order
        }
        return copy;
    }

    /* Massive amount of almost duplicate code
     * Needs to be refactored */
    /*private void enterOrder(Order o) {
		if (o.bidAsk().equals("B")) {
            for (int i = 0; i < bidList.size(); i++) {
        		if ( o.price() < bidList.get(i).price() ) {
        			bidList.add(i, o);
        		    return;
                }
            }
                bidList.add(o); // Insert at end of list
                return;
        } else {
            for (int i = 0; i < askList.size(); i++) {
            	if ( o.price() > askList.get(i).price() ) {
            		askList.add(i, o);
            	    return;
                }
            }
            askList.add(o); // Insert at end of list
            return;
        }
    }*/
    
    private void enterOrder(Order o) {
    	if (o.bidAsk().equals("B")) {
    		insert(o, bidList);
    	}
    	else if (o.bidAsk().equals("A")) {
    		insert(o, askList);
    	} else {
    		System.out.println("Error in EnterOrder");
    		System.exit(0);
    	}
    }
    
	private void insert(Order o, LinkedList<Order> book) {
		
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
		else if (o.bidAsk().equals("A")) {
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
			System.out.println("Error in Insert");
			System.exit(0);
		}
		
		
	}

	private void amendOrder(Order o) {

	}

    private void trade(Order o) {
        
    }
    
    private void offTrade(Order o) {
        
    }
    
    private void cancelTrade(Order o) {
        
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
    private int findByTransactionID(List<Order> list, long transactionID) {
    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).transactionID() == transactionID) {
    			return i;
    		}
    	}
    	return -1;
    }
    
	public void display() {
		//text display of order books for debugging
		clearConsole();
		System.out.println("bid\t\t\t\t\t|Ask");
		for (int i = 0; i < bidList.size() || i < askList.size(); i++) {
			if (i < bidList.size()) {
				System.out.print(bidList.get(i).bidID()+"\t");
				System.out.print(bidList.get(i).price()+"\t");
				System.out.print(bidList.get(i).volume() + "\t|");
			} else {
				System.out.print("\t\t\t\t\t\t|");
			}
			if (i < askList.size()) {
				System.out.print(askList.get(i).askID()+"\t");
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

