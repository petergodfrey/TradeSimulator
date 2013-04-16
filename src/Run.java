import java.io.*;

public class Run {
    
    public static void main(String[] args) {
       
        Reader reader = null;
       
        try {
            reader = new Reader("sample.csv");
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("IO Error");
            System.exit(0);
        }
                
        OrderBooks  orderBooks  = new OrderBooks("MQG");
        Stratergy   stratergy   = new DumbStratergy(orderBooks);
        TradeEngine tradeEngine = new TradeEngine();
        
        SignalGenerator signalGenerator = new SignalGenerator(reader, stratergy, orderBooks, tradeEngine);
    
        while (signalGenerator.advance() != SignalGenerator.SIMULATION_END){
            // Do nothing
        }
    
    
        // Something to visualise the order books at the end
        System.out.println("Ask Orders:");
        for ( Order o : orderBooks.askList() ) {
            System.out.println( o.toString() );
        }
        System.out.println();
        System.out.println("Bid Orders:");
        for ( Order o : orderBooks.bidList() ) {
            System.out.println( o.toString() );
        }
    }
    
}