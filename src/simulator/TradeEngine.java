package simulator;

import java.util.*;
import java.lang.Math;

public class TradeEngine {
    
	private OrderBooks       orderBooks;
	private ArrayList<Order> tradeList;
	
    public TradeEngine(OrderBooks orderBooks) {
        this.orderBooks = orderBooks;
        this.tradeList = new ArrayList<Order>();
    }
    
    /*
     * Matches orders and removes them from the order books.
     * When a match is found, a Trade is generated and added to the trade list.
     * 
     * Return Value:
     * Returns the number of trades that were generated
     */
    
    /*public void trade() {
    	if ( orderBooks.askListSize() > 0 && orderBooks.bidListSize() > 0 &&
    			orderBooks.spread() >= 0 ) {
    		Order bestBid = orderBooks.bestBidOrder();
    		Order bestAsk = orderBooks.bestAskOrder();
    		
    		//if (bestBid.volume() == bestAsk.volume()) {
    			addTrade(bestBid, bestAsk);
    			orderBooks.deleteOrder(bestBid);
    			orderBooks.deleteOrder(bestAsk);
    		//}
    	}
    }*/
    
    /*public void oneTrade(Order bid, Order ask) {
    	if (bid.volume() == ask.volume()) {
    		addTrade(bid, ask);
			orderBooks.deleteOrder(bid);
			orderBooks.deleteOrder(ask);
    	} else {
	    	if ( bid.volume() < ask.volume() ) {
	    		addTrade(bid, ask);
	    		ask.updateVolume( ask.volume() - bid.volume() );
	    		orderBooks.deleteOrder(bid);
	    	} else if ( ask.volume() < bid.volume() ) {
	    		addTrade(bid, ask);
	    		bid.updateVolume( bid.volume() - ask.volume() );
	    		orderBooks.deleteOrder(ask);
	    	}
	    	oneTrade();
		}
    	
    }*/
    
    public int trade() {
    	
    	int numberOfTrades = 0;
    	//Generate a trade transaction with the following properties:
		//    i.  Volume traded is the minimum volume of best bid and best ask orders.
		//    ii. The trading price is determined as:
    	int counter = 0;
    	while ( orderBooks.askListSize() > 0 && orderBooks.bidListSize() > 0 &&
    			orderBooks.spread() >= 0 ) {
    		counter++;
    		if (counter > 20) {
    			//hack to break loop
    			//fix later
    			break;
    		}
    		Order bestBid = orderBooks.bestBidOrder();
    		Order bestAsk = orderBooks.bestAskOrder();

            // Update the orderbook as follows:
    	    // 1. Remove best bid and best ask orders if both have the same volume.
    		if ( bestBid.volume() == bestAsk.volume() ) {
    			addTrade(bestBid, bestAsk);
    			orderBooks.deleteOrder(bestBid);
    			orderBooks.deleteOrder(bestAsk);
    		// 2. Otherwise, remove the order with the minimum volume (either best bid
            //    or best ask) from the orderbook, and update the volume of the other order
    		} else {
    	    	if ( bestBid.volume() < bestAsk.volume() ) {
    	    		addTrade(bestBid, bestAsk);
    	    		bestAsk.updateVolume( bestAsk.volume() - bestBid.volume() );
    	    		orderBooks.deleteOrder(bestBid);
    	    	} else if ( bestAsk.volume() < bestBid.volume() ) {
    	    		addTrade(bestBid, bestAsk);
    	    		bestBid.updateVolume( bestBid.volume() - bestAsk.volume() );
    	    		orderBooks.deleteOrder(bestAsk);
    	    	}
    		}
    		numberOfTrades++;
    	}
    	return numberOfTrades;		
    }
    
    /*
     * Creates a trade based on two orders and adds the trade to the tradeList
     * Assumes that the orders are the best bid and ask orders and that the spread
     * is <= 0
     * 
     * Parameters:
     * bid - The bid order to be traded
     * ask - the ask order to be traded
     */
    private void addTrade(Order bid, Order ask) {
    	
    	//System.out.println("Trade!");
    	
    	double tradePrice = bid.price();
    	if ( ask.isEarlier(bid) ) {
			tradePrice = ask.price();
		}
    	
    	double volume = Math.min(bid.volume(), ask.volume() );
    	
    	tradeList.add(new Order( bid.instrument(),
    			                 "",            // TODO Date & Time
    			                 "",
    			                 "TRADE",
    	           	             tradePrice,
    			                 volume,
    					         "",            // TODO Qualifiers
    					         1,             // TODO Transaction ID
    					         bid.bidID(),
    					         ask.askID(),
    					         "") );
    }
}