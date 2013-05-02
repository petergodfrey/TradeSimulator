package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
			System.out.printf("\r %s %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d", orderBooks.getSimulatedTime(),
					100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());

		}

		f.resetCSVColumns();//every CSV file may have different formatting

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
			System.out.printf("\r %s %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d", orderBooks.getSimulatedTime(),
					100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());
			}

		f.resetCSVColumns();//every CSV file may have different formatting

	}
	
	@Test
	public void testOrderBookOrderedInvariant() {
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
			testOrdered(orderBooks);
			System.out.printf("\r %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d",
					100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());
		}

		f.resetCSVColumns();//every CSV file may have different formatting

	}
	
	private void testOrdered(OrderBooks books) {
		List<Order> bid = books.bidList();
		List<Order> ask = books.askList();

		for (int i = 1; i < bid.size(); i++) {
			assertTrue(bid.get(i-1).price() >= bid.get(i).price());
		}
		for (int i = 1; i < ask.size(); i++) {
			assertTrue(ask.get(i-1).price() <= ask.get(i).price());
		}
	}

	@Test
	public void testOrderBookEntryUniquenessInvariant() {
		fail("Not yet implemented");
	}
}
