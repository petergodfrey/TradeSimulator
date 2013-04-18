package simulator;
import java.io.*;

public class SignalGenerator {

    public static final int ADVANCE_SUCCESS = 0;
    public static final int SIMULATION_END  = 1;

    private Reader      reader;
    private Strategy   stratergy;

    
    SignalGenerator(Reader reader, Strategy stratergy) {
        this.reader      = reader;
        this.stratergy   = stratergy;
    }
    
    public Order advance() {
    	Order o = stratergy.generateOrder();
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
    
    /*
     * This method advances the simulator by a single step
     */
   /* public Order advance() {
        Order o = stratergy.generateOrder();
        
        if (o == Order.NO_ORDER) {     // Check if an order was generated
            try {
                o = reader.next();     // No order from stratergy,
            } catch (IOException e) {  // so read one from file
                return SIMULATION_END;
            }
            if (o == Order.NO_ORDER) { // If all orders in the file have been exhausted
                return SIMULATION_END; // We have reached the end of the simulation
            }
        }
        
        orderBooks.processOrder(o); // Update the order books
        tradeEngine.trade();
        
        return o;
    }*/
    
}