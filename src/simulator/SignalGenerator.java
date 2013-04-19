package simulator;
import java.io.*;

import simulator.Strategy.Strategy;

public class SignalGenerator {
	
    private Reader      reader;
    private Strategy   strategy;

    
    public SignalGenerator(Reader reader, Strategy strategy) {
        this.reader      = reader;
        this.strategy   = strategy;
    }
    
    /*
     * This method advances the simulator by a single step
     */
    public Order advance() {
    	Order o = strategy.generateOrder();
    	if (o == Order.NO_ORDER) {
    		try {
				o = reader.next();
			} catch (IOException e) {
				System.out.println("Error in Reading File. Exiting");
				System.exit(0);
			}
    	}
    	
    	return o;
    }
    


    
}