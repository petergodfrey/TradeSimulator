package simulator;
import java.io.*;

import simulator.Strategy.Strategy;

public class SignalGenerator {

	private Reader      reader;
	private Strategy   strategy;

	private Factory f;


	public SignalGenerator(Reader reader, Strategy strategy, Factory f) throws IOException {
		this.reader      = reader;
		this.strategy   = strategy;
		this.f = f;

		//read first line and determine the index positions of columns
		f.setCSVColumns(reader.readLine()); // Read the initial line

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
	
	//selectively chooses so that TRADE, OFFTR, CANCEL_TRADE do not go into orderbooks
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
	
	//creates order from a CSV line
	private Order createOrder() throws IOException {

		String line = reader.readLine();      // Read a single line

		if (line == null) {                   // Check if end of file was reached
			return Order.NO_ORDER;
		}

		return f.makeOrderFromCSV(line);
	}


}