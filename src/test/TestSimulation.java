package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import simulator.Factory;
import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;
import simulator.SignalGenerator;
import simulator.TradeEngine;
import simulator.strategy.Strategy;

public class TestSimulation {

	String sample1FilePath = System.getProperty("user.dir") + "/Sample1.csv";
	String sample2FilePath = System.getProperty("user.dir") + "/Sample2.csv";
	String shortSampleFilePath = System.getProperty("user.dir") + "/shortSample.csv";
	OrderBooks orderBooks;
	Factory f;
	Reader CSV;
	Strategy strat;
	SignalGenerator SG;
	TradeEngine tradeEngine;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		f = new Factory();
		orderBooks = f.makeOrderBooks();
		tradeEngine = f.makeTradeEngine();
	}

	@Test
	public void testShortSampleSimulation() {

		try {
			CSV = f.makeReader(shortSampleFilePath);
			strat = f.makeNullStrategy();
			SG = new SignalGenerator(CSV, strat, f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Order o;
		while ((o = SG.advance()) != null) {
			orderBooks.processOrder(o);
			tradeEngine.trade();
			System.out.printf("\r %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d",
					100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());

		}
		//Evaluator eval = new Evaluator(strat, tradeEngine);
		//eval.evaluate();
		f.resetCSVColumns();//every CSV file may have different formatting
		//List<Trade> tradeList = tradeEngine.getTradeList();
		//for (Trade t :tradeList) {
		//System.out.println(t.toString());
		//}
	}

	@Test
	public void testSample1Simulation() {

		try {
			CSV = f.makeReader(sample1FilePath);
			strat = f.makeNullStrategy();
			SG = new SignalGenerator(CSV, strat, f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Order o;
		while ((o = SG.advance()) != null) {
			orderBooks.processOrder(o);
			tradeEngine.trade();
			System.out.printf("\r %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d",
					100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());
		}
		//Evaluator eval = new Evaluator(strat, tradeEngine);
		//eval.evaluate();
		f.resetCSVColumns();//every CSV file may have different formatting
		//List<Trade> tradeList = tradeEngine.getTradeList();
		//for (Trade t :tradeList) {
		//System.out.println(t.toString());
		//}
	}
}
