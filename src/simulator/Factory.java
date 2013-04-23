package simulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simulator.Strategy.NullStrategy;
import simulator.Strategy.Strategy;
import simulator.Strategy.DumbStrategy;

public class Factory {

	private OrderBooks books = null;
	private TradeEngine te = null;

	private static final double EMPTY_DOUBLE_FIELD = -1;
	private static final long   EMPTY_LONG_FIELD = -1;

	/* The index positions of each data field in the csv file */
	private int INSTRUMENT;
	private int DATE;
	private int TIME;
	private int RECORD_TYPE;
	private int PRICE;
	private int VOLUME;
	private int QUALIFIERS;
	private int TRANSACTION_ID;
	private int BID_ID;
	private int ASK_ID;
	private int BID_ASK;

	//orders index number
	private int bidID	= 0;
	private int askID	= 0;
	private int tradeID = 0;


	public OrderBooks makeOrderBooks() {
		if (books == null) {
			books = new OrderBooks();
		}
		return books;
	}

	public TradeEngine makeTradeEngine() {
		if (te == null) {
			te = new TradeEngine(makeOrderBooks(), this);
		}
		return te;
	}

	public Strategy makeNullStrategy() {
		return new NullStrategy();
	}

	public Strategy makeDumbStrategy() {
		return new DumbStrategy(makeOrderBooks());
	}

	public Reader makeReader(String filepath) throws FileNotFoundException, IOException {
		return new Reader(filepath);
	}



	public void setCSVColumns(String line) throws IOException {
		//read first line and determine the index positions of columns

		List<String> fieldNames = new ArrayList<String>();
		if (line != null) {
			// Remove the leading Hash symbol before splitting
			fieldNames = Arrays.asList( line.replace("#", "").split(",", -1) );
		}

		INSTRUMENT     = fieldNames.indexOf("Instrument");
		DATE           = fieldNames.indexOf("Date");
		TIME           = fieldNames.indexOf("Time");
		RECORD_TYPE    = fieldNames.indexOf("Record Type");
		PRICE          = fieldNames.indexOf("Price");
		VOLUME         = fieldNames.indexOf("Volume");
		QUALIFIERS     = fieldNames.indexOf("Qualifiers");
		TRANSACTION_ID = fieldNames.indexOf("Trans ID");
		BID_ID         = fieldNames.indexOf("Bid ID");
		ASK_ID         = fieldNames.indexOf("Ask ID");
		BID_ASK        = fieldNames.indexOf("Bid/Ask");

		// Check that all fields were successfully found
		if ( INSTRUMENT     == -1 ||
				DATE           == -1 ||
				TIME           == -1 ||
				RECORD_TYPE    == -1 ||
				PRICE          == -1 ||
				VOLUME         == -1 ||
				QUALIFIERS     == -1 ||
				TRANSACTION_ID == -1 ||
				BID_ID         == -1 ||
				ASK_ID         == -1 ||
				BID_ASK        == -1 ) {

			throw new IOException();
		}
	}

	public void resetCSVColumns() {
		INSTRUMENT     = -1;
		DATE           = -1;
		TIME           = -1;
		RECORD_TYPE    = -1;
		PRICE          = -1;
		VOLUME         = -1;
		QUALIFIERS     = -1;
		TRANSACTION_ID = -1;
		BID_ID         = -1;
		ASK_ID         = -1;
		BID_ASK        = -1; 
	}
	
	//makes actual order object
	public Order makeOrder(String date, String time,
			String recordType, double price, double volume,
			String qualifiers, long   transactionID,
			long   bidID, long   askID,
			String bidAsk) {

		return new Order(date, time, recordType, price,
				volume, qualifiers, transactionID, bidID, askID, bidAsk);
	}
	
	//converts string to order object, calling makeOrder()
	public Order makeOrderFromCSV(String line) {

		if (line == null) {                   // Check if end of file was reached
            return Order.NO_ORDER;
        }
        
        String[] entry = line.split(",", -1); // Break line into individual fields

        return makeOrder(
                entry[DATE],
                entry[TIME],
                entry[RECORD_TYPE],
                parseDouble(entry[PRICE]),
                parseDouble(entry[VOLUME]),
                entry[QUALIFIERS],
                parseLong(entry[TRANSACTION_ID]),
                parseLong(entry[BID_ID]),
                parseLong(entry[ASK_ID]),
                entry[BID_ASK]);
	}
	
    // Parser which handles the case of an empty string
    private double parseDouble(String s) {
        if ( s.equals("") ) {
            return EMPTY_DOUBLE_FIELD;
        } else {
            return Double.parseDouble(s);    
        }
    }
    
    // Parser which handles the case of an empty string
    private long parseLong(String s) {
        if ( s.equals("") ) {
            return EMPTY_LONG_FIELD;
        } else {
            return Long.parseLong(s);    
        }
    }

}
