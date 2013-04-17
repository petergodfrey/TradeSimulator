package simulator;
import java.io.*;

public class Run {
    
    public static void main(String[] args) {
       
        Reader reader = null;
       
        // Create the Reader
        try {
            reader = new Reader("sample1.csv");
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("IO Error");
            System.exit(0);
        }
        
        // Create the objects required by the SignalGenerator
        OrderBooks  orderBooks  = new OrderBooks("MQG");
        Stratergy   stratergy   = new DumbStratergy(orderBooks);
        TradeEngine tradeEngine = new TradeEngine(orderBooks);
        
        SignalGenerator signalGenerator = new SignalGenerator(reader, stratergy, orderBooks, tradeEngine);
        
        System.out.print("Running simulation........... ");
        
        while (signalGenerator.advance() != SignalGenerator.SIMULATION_END){
            // Do nothing
        }
        
        System.out.println("Finished");
        
    }
    
}