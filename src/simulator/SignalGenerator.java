package simulator;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simulator.Strategy.Strategy;

public class SignalGenerator {
	
    private static final double EMPTY_DOUBLE_FIELD = -1;
    private static final long   EMPTY_LONG_FIELD = -1;
	
	/* The index positions of each data field in the csv file */
    private final int INSTRUMENT;
    private final int DATE;
    private final int TIME;
    private final int RECORD_TYPE;
    private final int PRICE;
    private final int VOLUME;
    private final int QUALIFIERS;
    private final int TRANSACTION_ID;
    private final int BID_ID;
    private final int ASK_ID;
    private final int BID_ASK;
	
    private Reader      reader;
    private Strategy   strategy;
    
    private Factory f;

    
    public SignalGenerator(Reader reader, Strategy strategy, Factory f) throws IOException {
        this.reader      = reader;
        this.strategy   = strategy;
    	this.f = f;
    	
        //read first line and determine the index positions of columns
        String line = reader.readLine(); // Read the initial line
        
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
    
    /*
     * This method advances the simulator by a single step
     */
    public Order advance() {
    	Order o = strategy.generateOrder();
    	if (o == Order.NO_ORDER) {
    		try {
				o = CSVNext();
			} catch (IOException e) {
				System.out.println("Error in Reading File. Exiting");
				System.exit(0);
			}
    	}
    	return o;
    }

	private Order CSVNext() throws IOException {
		Order o = createOrder();
    	while (o != null && (o.recordType().equals("TRADE") ||
    			o.recordType().equals("OFFTR") ||
    			o.recordType().equals("CANCEL_TRADE") ||
    			o.recordType().equals("DELETE") ||
    			o.recordType().equals("AMEND"))) {
    		o = createOrder();
    	}
    	return o;
	}
    
    private Order createOrder() throws IOException {
    	
        String line = reader.readLine();      // Read a single line
        
        if (line == null) {                   // Check if end of file was reached
            return Order.NO_ORDER;
        }
        
        String[] entry = line.split(",", -1); // Break line into individual fields
            	
        return f.makeOrder(
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