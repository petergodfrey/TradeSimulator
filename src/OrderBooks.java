import java.util.*;

/* This class manages the order books for the simulator.
 * The order books consist of two lists of orders, one for
 * Ask orders and one for Bid orders.
 * At this stage, the order books only contain orders for a single
 * Instrument.
 */

public class OrderBooks {
    
    /* Using an array list to allow efficient look up
     * since the order books will be sorted by price/time
     */
    private ArrayList<Order> bidList;
    private ArrayList<Order> askList;
    private String security;          /* The security which the order books represent */
    
    public OrderBooks(String security) {
        bidList       = new ArrayList<Order>();
        askList       = new ArrayList<Order>();
        this.security = security;
    }
    
    public void processOrder(Order o) throws UnsupportedOperationException {
    	if ( o.recordType().equals("ENTER") ) {
        	enterOrder(o);
        } else if ( o.recordType().equals("DELETE") ) {
    		deleteOrder(o);
        } else if ( o.recordType().equals("AMEND") ) {
    		amendOrder(o);
        } else if ( o.recordType().equals("TRADE") ) {
    		trade(o);
        } else if ( o.recordType().equals("OFFTR") ) {
    		offTrade(o);
        } else if ( o.recordType().equals("CANCEL_TRADE") ) {
    		cancelTrade(o);
    	} else {
    		//Record type is unsupported, Sirca data should never reach here
    		throw new UnsupportedOperationException();
    	}
    }
    
    // Returns the current spread
    public double spread() {
        return 1;
    }
    // Returns the lowest ask (sell) price
    public double bestAskPrice() {
        return 1;
    }
    // Returns the highest bid (buy) price
    public double bestBidPrice() {
        return 1;
    }
    
    // Returns a COPY of the bid list
    public List<Order> bidList() {
        return copyList(bidList);
    }
    
    // Returns a COPY of the ask list
    public List<Order> askList() {
        return copyList(askList);
    }
    
    // Returns the size of the bid list
    public int bidListSize() {
        return bidList.size();
    }
    
    // Returns the size of the ask list
    public int askListSize() {
        return askList.size();
    }
        
    
    /* Private Methods */

    // Takes an ArrayList of Orders and returns a deep copy
    private ArrayList<Order> copyList(ArrayList<Order> list) {
        ArrayList<Order> copy = new ArrayList<Order>();
        for (Order o : list) {
            copy.add( new Order(o) ); // Add a copy of the order
        }
        return copy;
    }

    /* Massive amount of almost duplicate code
     * Needs to be refactored */
    private void enterOrder(Order o) {
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
    }
    
    // Delete an order from the order books
    private boolean deleteOrder(Order o) {
        if ( o.bidAsk().equals("B") ) {
            return bidList.remove(o);
        } else if ( o.bidAsk().equals("A") ) {
            return askList.remove(o);
        } else {
            return false;
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
    
}

