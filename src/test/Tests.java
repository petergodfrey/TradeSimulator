package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import simulator.Evaluator;
import simulator.Factory;
import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;
import simulator.SignalGenerator;
import simulator.Trade;
import simulator.TradeEngine;
import simulator.Strategy.Strategy;

public class Tests {

	String sample1FilePath = System.getProperty("user.dir") + "/Sample1.csv";
	String sample2FilePath = System.getProperty("user.dir") + "/Sample2.csv";
	String shortSampleFilePath = System.getProperty("user.dir") + "/shortSample.csv";
	OrderBooks TE;
	Factory factory;

	/*
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	 */

	@Before
	public void setUp() throws Exception {
		factory = new Factory();
		//Order o1 = new Order();
	}

	@Test
	public void testSimulation() {
		Reader CSV = null;
		Strategy strat = null;
		SignalGenerator SG = null;
		try {
			CSV = factory.makeReader(shortSampleFilePath);
			strat = factory.makeNullStrategy();
			SG = new SignalGenerator(CSV, strat, factory);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create the objects required by the SignalGenerator
		OrderBooks  orderBooks  = factory.makeOrderBooks();
		TradeEngine tradeEngine = factory.makeTradeEngine();

		System.out.println("Running simulation ");

		Order o;
		while ((o = SG.advance()) != null) {
			//one iteration equals one order being processed and traded
			orderBooks.processOrder(o);
			tradeEngine.trade();
			//orderBooks.display();
			//System.out.println(orderBooks.getSimulatedTime());
			//System.out.printf("\r %.2f percent done",
			//		100*((float)CSV.getProgress()/(float)CSV.getFileSize()));


		}
		Evaluator eval = new Evaluator(strat, tradeEngine);
		System.out.println("\nFinished Simulation");
		eval.evaluate();
		factory.resetCSVColumns();//every CSV file may have different formatting
		List<Trade> tradeList = tradeEngine.getTradeList();
		for (Trade t :tradeList) {
			System.out.println(t.toString());
		}

	}

	@Test
	public void testCSVChooseFile() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		assertEquals(sample1FilePath, CSV.getFilePath());
	}

	@Test
	public void testCSVReadLine() {

	}

	@Test
	public void testOrderGeneration() {
		Order order = new Order("time", "recordType", 0.00, 0.00, "qualifiers", 1234, "bidAsk");
		//assertEquals("instrument", order.instrument());
		assertEquals("time", order.time());
		assertEquals("recordType", order.recordType());
		assertEquals(new Double(0.00), order.price());
		assertEquals(new Double(0.00), order.volume());
		assertEquals("qualifiers", order.qualifiers());
		assertEquals(1234, order.ID());
		assertEquals("bidAsk", order.bidAsk());	
	}

	@Test
	public void testOrderAttributeConstraintENTER() {
		OrderBooks orderbooks = factory.makeOrderBooks();		
		//bidListArrayList<Order>
		//assertEquals();
		//assertEquals();
	}

	@Test
	public void testOrderAttributeConstraintDELETE() {

	}

	@Test
	public void testOrderAttributeConstraintTRADE() {

	}

	@Test
	public void testOrderAttributeConstraintAMEND() {

	}

	@Test
	public void testOrderAttributeConstraintOFFTR() {

	}

	@Test
	public void testOrderAttributeConstraintCANCELTRADE() {

	}

	@Test
	public void testOrderBookOrderedInvariant() {

	}

	@Test
	public void testOrderBookEntryUniquenessInvariant() {

	}

	/* this is done with other functions
	@Test
	public void testOrderBookProcess() {

	}
	 */
	@Test
	public void testOrderBookGetBid() {
		Order bid1 = new Order("00:00:00.000", "ENTER", 67.76, 1959.0, new String(), new Long("6239925033925459786"), "B");
		Order bid2 = new Order("00:00:00.000", "ENTER", 67.760, 15,new String(),new Long("6239925033924850752"),"B");
		Order bid3 = new Order("00:00:00.000", "ENTER", 67.380,111,new String(), new Long("6239925033923871154"), "B");
		OrderBooks b = factory.makeOrderBooks(); 
		b.processOrder(bid1);
		b.processOrder(bid2);
		b.processOrder(bid3);
		assertEquals(3, b.bidListSize());
		assertEquals(bid1, b.bestBidOrder());
	}

	@Test
	public void testOrderBookGetAsk() {		
		OrderBooks b = factory.makeOrderBooks();
		Order ask1 = new Order("00:00:00.000", "ENTER", 67.900, 409, new String(), new Long("6238329642550435411"), "A");
		Order ask2 = new Order("00:00:00.000", "ENTER", 67.900, 200, new String(), new Long("6239528659982899884"), "A");
		Order ask3 = new Order("00:00:00.000", "ENTER", 67.940, 300, new String(), new Long("6239925033924584462"), "A");
		b.processOrder(ask1);
		b.processOrder(ask2);
		b.processOrder(ask3);
		assertEquals(3, b.askListSize());		
		assertEquals(ask1, b.bestAskOrder());
	}

	@Test
	public void testOrderBookAdd() {
		Order bid = new Order("00:00:00.000", "ENTER", 67.76, 1959.0 , new String(), new Long("6239925033925459786"), "B");
		OrderBooks b = factory.makeOrderBooks(); 
		b.processOrder(bid);
		assertEquals(1, b.bidListSize());
		assertEquals(bid, b.bestBidOrder());
		Order bid2 = new Order("00:00:00.000", "ENTER", 67.760, 15,new String(),new Long("6239925033924850752"),"B");
		b.processOrder(bid2);
		assertEquals(2, b.bidListSize());

		Order ask1 = new Order("00:00:00.000","ENTER", 67.900, 409,new String(), new Long("6238329642550435411"), "A");
		Order ask2 = new Order("00:00:00.000", "ENTER", 67.900, 200, new String(), new Long("6239528659982899884"), "A");
		b.processOrder(ask1);
		assertEquals(1, b.askListSize());
		assertEquals(ask1, b.bestAskOrder());
		b.processOrder(ask2);
		assertEquals(2, b.askListSize());
	}

	@Test
	public void testOrderBookDelete() {
		Order bid = new Order("00:00:00.000", "ENTER", 67.76, 1959.0 , new String(), new Long("6239925033925459786"), "B");
		OrderBooks b = factory.makeOrderBooks(); 
		b.processOrder(bid);
		assertEquals(1, b.bidListSize());
		assertEquals(bid, b.bestBidOrder());
		Order bidD = new Order("00:00:00.000", "DELETE", 67.76, 1959.0 , new String(), new Long("6239925033925459786"), "B");
		b.processOrder(bidD);
		assertEquals(0, b.bidListSize());

		Order ask = new Order("00:00:00.000","ENTER", 67.900, 409,new String(), new Long("6238329642550435411"), "A");
		b.processOrder(ask);
		assertEquals(1, b.askListSize());
		assertEquals(ask, b.bestAskOrder());
		Order askD = new Order("00:00:00.000","DELETE", 67.900, 409,new String(), new Long("6238329642550435411"), "A");	
		b.processOrder(askD);
		assertEquals(0, b.askListSize());
	}

	@Test
	public void testOrderBookAmend() {
		// related functions are implementing
	}

	@Test
	public void testOrderBookTrade() {
		// related functions are implementing
	}

	@Test
	public void testOrderBookOffTrade() {
		// related functions are implementing
	}

	@Test
	public void testOrderBookCancelTrade() {
		// related functions are implementing
	}

	@Test
	public void testGeneratorGetTimeStamp() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		Strategy strategy= factory.makeDumbStrategy();
		SignalGenerator sg = null;
		try {
			sg = new SignalGenerator(CSV, strategy, factory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(new String("00:00:00.000"), sg.advance().time());
	}

	@Test
	public void testGeneratorGetRecordType() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		Strategy strategy= factory.makeDumbStrategy();
		SignalGenerator sg = null;
		try {
			sg = new SignalGenerator(CSV, strategy, factory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(new String("ENTER"), sg.advance().recordType());
	}

	@Test
	public void testGeneratorGetPrice() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		Strategy strategy= factory.makeDumbStrategy();
		SignalGenerator sg = null;
		try {
			sg = new SignalGenerator(CSV, strategy, factory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(new Double(67.760), sg.advance().price());
	}

	@Test
	public void testGeneratorGetVolume() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		Strategy strategy= factory.makeDumbStrategy();
		SignalGenerator sg = null;
		try {
			sg = new SignalGenerator(CSV, strategy, factory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(new Double(1959), sg.advance().volume());
	}

	@Test
	public void testGeneratorGetBidAsk() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		Strategy strategy= factory.makeDumbStrategy();
		SignalGenerator sg = null;
		try {
			sg = new SignalGenerator(CSV, strategy, factory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(new String("B"), sg.advance().bidAsk());
	}

	@Test
	public void testGeneratorGetQualifier() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		Strategy strategy= factory.makeDumbStrategy();
		SignalGenerator sg = null;
		try {
			sg = new SignalGenerator(CSV, strategy, factory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(new String(), sg.advance().qualifiers());
	}

}

