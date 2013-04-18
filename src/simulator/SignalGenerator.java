package simulator;
import java.io.*;

public class SignalGenerator {

    public static final int ADVANCE_SUCCESS = 0;
    public static final int SIMULATION_END  = 1;

    private Reader      reader;
    private Strategy   strategy;

    
    SignalGenerator(Reader reader, Strategy stratergy) {
        this.reader      = reader;
        this.strategy   = stratergy;
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