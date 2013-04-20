package simulator;
import java.io.*;
import java.util.*;

public class Reader {
    
    private static final double EMPTY_DOUBLE_FIELD = -1;
    private static final long   EMPTY_LONG_FIELD = -1;
    
    private final String filepath;
	
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
    
    private Factory f;
    
    private BufferedReader reader;
    private BufferedReader sizeReader;
    private int size = -1;//header line is counted, so start with -1
    private int progress = 0;
    
    public Reader(String path, Factory f) throws IOException, FileNotFoundException {
        
    	this.f = f;
        reader = new BufferedReader( new FileReader(path) );
        sizeReader = new BufferedReader( new FileReader(path) );
        
        for (; sizeReader.readLine() != null; size++) {
        }
        this.filepath = path;
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
    
    public int getFileSize() {
    	return size;
    }
    
    public int getProgress() {
    	return progress;
    }
    
    public String getFilePath() {
    	return this.filepath;
    }
    
    public Order next() throws IOException {
    	Order o = createOrder();
    	progress++;
    	while (o != null && (o.recordType().equals("TRADE") ||
    			o.recordType().equals("OFFTR") ||
    			o.recordType().equals("CANCEL_TRADE") ||
    			o.recordType().equals("DELETE") ||
    			o.recordType().equals("AMEND"))) {
    		o = createOrder();
    		progress++;
    	}
    	return o;
    }

    /* Returns the next order from the file
     * Throws an IOException if a line cannot be read
     */
    private Order createOrder() throws IOException {
    	
        String line = reader.readLine();      // Read a single line
        
        if (line == null) {                   // Check if end of file was reached
            return Order.NO_ORDER;
        }
        
        String[] entry = line.split(",", -1); // Break line into individual fields
            	
        return f.makeOrder(entry[INSTRUMENT],
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