package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import simulator.Order;
import simulator.Reader;

public class TestReader {
	
	public static void run() {
		
		System.out.print("Testing Reader class..... ");
		
		Reader r = null;
		
		try {
			r = new Reader("sample1.csv");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace(System.err);
			System.exit(1);
		} catch (IOException e2) {
			e2.printStackTrace(System.err);
			System.exit(1);
		}
		
		Order o = Order.NO_ORDER;
		try {
    		o = r.next();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		
		assert( o.instrument().equals("CBA") );
		assert( o.date().equals("20130304") );
		assert( o.time().equals("00:00:00.000") );
		assert( o.recordType().equals("ENTER") );
		assert( o.price() == 67.760 );
		assert( o.volume() == 1959 );
		assert( o.qualifiers().equals("") );
		//assert( o.transactionID().equals(6239925033925459786) )

		if (o == Order.NO_ORDER) {
			System.err.println("Error creating order");
			System.exit(1);
		}
		
		
		while (o != Order.NO_ORDER) {
			try {
	    		o = r.next();
			} catch (IOException e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
			if (o != Order.NO_ORDER) {
    			assert( !o.recordType().equals("TRADE") );
			}
		}
		
		System.out.println("Passed!");
	}
	
}