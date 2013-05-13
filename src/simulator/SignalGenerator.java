package simulator;
import java.io.*;

import simulator.strategy.Strategy;

public class SignalGenerator {

	private Reader      reader;
	private Strategy   strategy;
	private Factory f;


	public SignalGenerator(Reader reader, Strategy strategy, Factory f) throws IOException {
		this.reader      = reader;
		this.strategy   = strategy;
		this.f = f;
		
		f.resetCSVColumns();//every CSV file may have different formatting
		//read first line and determine the index positions of columns
		f.setCSVColumns(reader.readLine()); // Read the initial line

	}

	/*
	 * This method advances the simulator by a single step
	 */
	public Order advance() {
		Order o = strategy.submitOrder();    
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

	//selectively chooses so that TRADE, OFFTR, CANCEL_TRADE do not go into orderbooks
	private Order CSVNext() throws IOException {
		Order o = createOrder();
		return o;
	}

	//creates order from a CSV line
	private Order createOrder() throws IOException {
		Order o = null;
		String line = reader.readLine();      // Read a single line

		if (line == null) {                   // Check if end of file was reached
			o = Order.NO_ORDER;
		} else {

			o = f.makeOrderFromCSV(line);

			if (o == null) {
				o = createOrder();
			}
		}
		return o;
	}


}