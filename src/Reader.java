import java.io.*;
import java.lang.*;

public class Reader {
    
    /* The positions of each data field in the csv file */
    private static final int SECURITY       = 0;
    private static final int DATE           = 1;
    private static final int TIME           = 2;
    private static final int RECORD_TYPE    = 3;
    private static final int PRICE          = 4;
    private static final int VOLUME         = 5;
    private static final int VALUE          = 6;
    private static final int TRANSACTION_ID = 7;
    private static final int BID_ID         = 8;
    private static final int ASK_ID         = 9;
    private static final int BID_ASK        = 10;
    
    private static final double EMPTY_DOUBLE_FIELD = -1;
    private static final long EMPTY_LONG_FIELD = -1;
    
	private BufferedReader reader;
    
    Reader(String path) throws FileNotFoundException, IOException {
        try {
        	reader = new BufferedReader( new FileReader(path) );
        } catch (FileNotFoundException e) {
            throw e;
        }
        try {
            reader.readLine(); // Read past the initial line
        } catch (IOException e){
            throw e;
        }
    }

    /* Returns the next order from the file
     * Throws an IOException if a line cannot be read
     */
    public Order next() throws IOException {
    	
        String line;
        String[] entry;
        
        try {
    	    line = reader.readLine(); // Read in a single line
        } catch (IOException e) {
            throw e;
        }
        if (line == null) {           // Check if end of file is reached
            return Order.NO_ORDER;
        }
        entry = line.split(",");      // Break line into individual fields
        
        // Ignore Trades
        while ( entry[RECORD_TYPE].equals("TRADE") ) {
            try {                         // Entry was a trade,
        	    line = reader.readLine(); // read another
            } catch (IOException e) {
                throw e;
            }
            if (line == null) {           // Check if end of file is reached
                return Order.NO_ORDER;
            }
            entry = line.split(",");      // Break line into individual fields
        }
        
    	return new Order (entry[SECURITY],
                          entry[DATE],
                          entry[TIME],
                          entry[RECORD_TYPE],
                          parseDouble(entry[PRICE]),
                          parseDouble(entry[VOLUME]),
                          parseDouble(entry[VALUE]),
                          parseLong(entry[TRANSACTION_ID]),
                          parseLong(entry[BID_ID]),
                          parseLong(entry[ASK_ID]),
                          entry[BID_ASK]);
    }
        
    private String readLine () throws IOException {
	    String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw e;
        }
        return line;
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