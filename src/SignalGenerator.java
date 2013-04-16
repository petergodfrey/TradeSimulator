import java.io.*;

public class SignalGenerator {

    public static final int ADVANCE_SUCCESS = 0;
    public static final int SIMULATION_END  = 1;

    private Reader      reader;
    private Stratergy   stratergy;
    private OrderBooks  orderBooks;
    private TradeEngine tradeEngine;
    
    SignalGenerator(Reader reader, Stratergy stratergy, OrderBooks orderBooks, TradeEngine tradeEngine) {
        this.reader      = reader;
        this.stratergy   = stratergy;
        this.orderBooks  = orderBooks;
        this.tradeEngine = tradeEngine;
    }
    
    /*
     * This method advances the simulator by a single step
     */
    public int advance() {
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
        
        return ADVANCE_SUCCESS;
    }
    
}