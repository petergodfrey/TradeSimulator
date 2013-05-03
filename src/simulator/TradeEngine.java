package simulator;

import java.util.*;
import java.lang.Math;

public class TradeEngine {
    
	private OrderBooks       orderBooks;
	private ArrayList<Trade> tradeList;
	private Factory f;
	private String openTrading = "10:00:00.000";
	
	private long sum;
	
    public TradeEngine(OrderBooks orderBooks, Factory f) {
        this.orderBooks = orderBooks;
        this.tradeList = new ArrayList<Trade>();
        this.f = f;
    }
    
    private boolean timeToTrade(String currentTime, String openingTime) {
    	return (OrderBooks.convertTimeToMilliseconds(currentTime) >= 
    			OrderBooks.convertTimeToMilliseconds(openingTime));
    }
    
    public boolean setOpenTradingTime(String newOpening) {
    	try {
    		OrderBooks.convertTimeToMilliseconds(newOpening);
    	} catch (Exception e) {
    		return false;
    	}
    	this.openTrading = newOpening;
    	return true;
    }
    
    public void resetTradeEngine() {
    	tradeList = new ArrayList<Trade>();
    	sum = 0;
    }
    
    /*
     * Matches orders and removes them from the order books.
     * When a match is found, a Trade is generated and added to the trade list.
     * 
     * Return Value:
     * Returns the number of trades that were generated
     */
    
    public void trade() {
    	if (!timeToTrade(orderBooks.getSimulatedTime(), openTrading)) {
    		//if not open trading time, don't trade
    		return;
    	}
    	
    	//Generate a trade transaction with the following properties:
		//    i.  Volume traded is the minimum volume of best bid and best ask orders.
		//    ii. The trading price is determined as:

    	if (orderBooks.spread() < 0) {
    		//if it is open trading session, but spread is < 0, can't trade
    		return;
    	}
    	//there is a trade available now, falls into 2 categories
    	//equal volume or not
    	//if equal volume, make trade and both bid and ask from orderbook
    	//if not, update volume of one and remove the other
    	//then recurse to see if there is another trade available
		Order bestBid = orderBooks.bestBidOrder();
		Order bestAsk = orderBooks.bestAskOrder();
		
		if (bestBid.volume() > bestAsk.volume()) {
			bestBid.updateVolume(bestBid.volume() - bestAsk.volume());
			orderBooks.deleteOrder(bestAsk);
			addTrade(bestBid, bestAsk);
			trade();

		}
		else if (bestBid.volume() < bestAsk.volume()) {
			bestAsk.updateVolume(bestAsk.volume() - bestBid.volume());
			orderBooks.deleteOrder(bestBid);
			addTrade(bestBid, bestAsk);
			trade();

		} else {
			orderBooks.deleteOrder(bestBid);
			orderBooks.deleteOrder(bestAsk);
			addTrade(bestBid, bestAsk);
		}

    	
 /*   	while ( orderBooks.askListSize() > 0 && orderBooks.bidListSize() > 0 &&
    			orderBooks.spread() >= 0 ) {

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
    	}
    	return;		*/
    }
    
    public ArrayList<Trade> getTradeList () {
    	return tradeList;
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
    	
    	double tradePrice = ask.price();//always trades at ask price
    	
    	int volume = Math.min(bid.volume(), ask.volume());
    	
    	sum += tradePrice;
    	
    	tradeList.add(f.makeTrade(orderBooks.getSimulatedTime(),
    			                 "TRADE",
    	           	             tradePrice,
    			                 volume,
    					         "",            // TODO Qualifiers
    					         "", bid, ask) );
    }
    
    public double avg() {
    	return (double)(sum/tradeList.size());
    }

}