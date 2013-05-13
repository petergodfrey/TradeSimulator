package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import simulator.Factory;
import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;
import simulator.SignalGenerator;
import simulator.Trade;
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
		System.out.println("Testing shortSampleSimulation");
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
			//System.out.printf("\r %s %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d", orderBooks.getSimulatedTime(),
			//		100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());

		}

		f.resetCSVColumns();//every CSV file may have different formatting

	}

	@Test
	public void testSample1Simulation() {
		System.out.println("Testing SampleSimulation");
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
			//System.out.printf("\r %s %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d", orderBooks.getSimulatedTime(),
			//		100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());
		}

		f.resetCSVColumns();//every CSV file may have different formatting

	}

	@Test
	public void testOrderBookOrderedInvariant() {
		System.out.println("Testing Orderbook Ordering");
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
			//System.out.printf("\r %.2f percent done, bidSize: %d. askSize: %d, tradeSize: %d",
			//		100*((float)CSV.getProgress()/(float)CSV.getFileSize()), orderBooks.bidListSize(), orderBooks.askListSize(), tradeEngine.getTradeList().size());
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
		System.out.println("Testing ID Uniqueness");
		try {
			CSV = f.makeReader(sample1FilePath);
			strat = f.makeNullStrategy();
			SG = new SignalGenerator(CSV, strat, f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Order o;
		int counter = 0;
		while ((o = SG.advance()) != null) {
			orderBooks.processOrder(o);
			tradeEngine.trade();
			System.out.println(counter++);
			testOrderBookIDUniqueness(orderBooks);


		}
	}

	private void testOrderBookIDUniqueness(OrderBooks books) {

		List<Order> bid = books.bidList();
		List<Order> ask = books.askList();
		Set<Long> bidSet = new HashSet<Long>();
		Set<Long> askSet = new HashSet<Long>();
		for (int i = 0; i < bid.size(); i++) {
			//check that it is not currently in the set
			//add it in if not
			assertFalse(bidSet.contains(bid.get(i).ID()));
			bidSet.add(bid.get(i).ID());
		}
		for (int i = 0; i < ask.size(); i++) {
			assertFalse(askSet.contains(ask.get(i).ID()));
			askSet.add(ask.get(i).ID());
		}
	}

	@Test
	public void testManualInputs() {

		System.out.println("Testing manualInputs");
		Order o0 = new Order("00:00:00.000","ENTER",0.040,100000,""  ,new Long("6224475796041657973"),"B");
		Order o1 = new Order("00:00:00.000","ENTER",0.075, 41024,""  ,new Long("6224475796041587834"),"A");
		Order o2 = new Order("00:00:00.000","ENTER",0.078, 45000,""  ,new Long("6225279539040458763"),"A");
		Order o3 = new Order("00:00:00.000","ENTER",0.094, 39998,""  ,new Long("6219341351617764647"),"A");
		Order o4 = new Order("13:08:34.603","ENTER",0.074, 10000,""  ,new Long("6230017334645827603"),"A");
		Order o5 = new Order("13:13:19.953","ENTER",0.074, 55000,""  ,new Long("6230017334645856696"),"B");
		Trade t0 = new Trade("13:13:19.953","TRADE",0.074, 10000,"",new Long("0"), "", o5,o4);
		Order o6 = new Order("13:14:37.327","AMEND",0.074, 45000,""  ,new Long("6225279539040458763"),"A");
		Trade t1 = new Trade("13:14:37.327","TRADE",0.074, 45000,"",new Long("1"), "", o5,o6);

		{
			//initial conditions
			assertEquals(orderBooks.bidListSize(), 0);
			assertEquals(orderBooks.askListSize(), 0);
		}
		{
			//process first order into orderbooks
			orderBooks.processOrder(o0);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 0);
		}
		{
			//attempt to trade
			tradeEngine.trade();
			assertEquals(tradeEngine.getTradeList().size(),0);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 0);
		}
		{
			//process second order into orderbooks
			orderBooks.processOrder(o1);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 1);
			compareOrders(orderBooks.askList().get(0), (o1));
		}
		{
			//attempt to trade
			tradeEngine.trade();
			assertEquals(tradeEngine.getTradeList().size(),0);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 1);
			compareOrders(orderBooks.askList().get(0), (o1));
		}
		{
			//process third order into orderbooks
			orderBooks.processOrder(o2);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 2);
			compareOrders(orderBooks.askList().get(0), (o1));
			compareOrders(orderBooks.askList().get(1), (o2));
		}
		{
			//attempt to trade
			tradeEngine.trade();
			assertEquals(tradeEngine.getTradeList().size(),0);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 2);
			compareOrders(orderBooks.askList().get(0), (o1));
			compareOrders(orderBooks.askList().get(1), (o2));
		}
		{
			//process fourth order into orderbooks
			orderBooks.processOrder(o3);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 3);
			compareOrders(orderBooks.askList().get(0), (o1));
			compareOrders(orderBooks.askList().get(1), (o2));
			compareOrders(orderBooks.askList().get(2), (o3));
		}
		{
			//attempt to trade
			tradeEngine.trade();
			assertEquals(tradeEngine.getTradeList().size(),0);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 3);
			compareOrders(orderBooks.askList().get(0), (o1));
			compareOrders(orderBooks.askList().get(1), (o2));
			compareOrders(orderBooks.askList().get(2), (o3));
		}
		{
			//process fifth order into orderbooks
			orderBooks.processOrder(o4);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 4);
			compareOrders(orderBooks.askList().get(0), (o4));
			compareOrders(orderBooks.askList().get(1), (o1));
			compareOrders(orderBooks.askList().get(2), (o2));
			compareOrders(orderBooks.askList().get(3), (o3));
		}
		{
			//attempt to trade
			tradeEngine.trade();
			assertEquals(tradeEngine.getTradeList().size(),0);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));

			assertEquals(orderBooks.askList().size(), 4);
			compareOrders(orderBooks.askList().get(0), (o4));
			compareOrders(orderBooks.askList().get(1), (o1));
			compareOrders(orderBooks.askList().get(2), (o2));
			compareOrders(orderBooks.askList().get(3), (o3));
		}
		{
			//process sixth order into orderbooks
			orderBooks.processOrder(o5);

			assertEquals(orderBooks.bidList().size(), 2);
			compareOrders(orderBooks.bidList().get(0), (o5));
			compareOrders(orderBooks.bidList().get(1), (o0));
			//prior to trade, the volume it has
			assertEquals(orderBooks.bidList().get(0).volume(), 55000);

			assertEquals(orderBooks.askList().size(), 4);
			compareOrders(orderBooks.askList().get(0), (o4));
			compareOrders(orderBooks.askList().get(1), (o1));
			compareOrders(orderBooks.askList().get(2), (o2));
			compareOrders(orderBooks.askList().get(3), (o3));
			//prior to trade, the volume it has
			assertEquals(orderBooks.askList().get(0).volume(), 10000);
		}
		{
			//attempt to trade
			tradeEngine.trade();
			assertEquals(tradeEngine.getTradeList().size(),1);
			compareTrades(tradeEngine.getTradeList().get(0),t0);

			assertEquals(orderBooks.bidList().size(), 2);
			compareOrders(orderBooks.bidList().get(0), (o5));
			compareOrders(orderBooks.bidList().get(1), (o0));
			assertEquals(orderBooks.bidList().get(0).volume(), 45000);

			assertEquals(orderBooks.askList().size(), 3);
			compareOrders(orderBooks.askList().get(0), (o1));
			compareOrders(orderBooks.askList().get(1), (o2));
			compareOrders(orderBooks.askList().get(2), (o3));
		}
		{
			//process seventh order into orderbooks
			orderBooks.processOrder(o6);

			assertEquals(orderBooks.bidList().size(), 2);
			compareOrders(orderBooks.bidList().get(0), (o5));
			compareOrders(orderBooks.bidList().get(1), (o0));
			//prior to trade, the volume it has
			assertEquals(orderBooks.bidList().get(0).volume(), 45000);

			assertEquals(orderBooks.askList().size(), 3);
			compareOrders(orderBooks.askList().get(0), (o6));
			compareOrders(orderBooks.askList().get(1), (o1));
			compareOrders(orderBooks.askList().get(2), (o3));
			//prior to trade, the volume it has
			assertEquals(orderBooks.askList().get(0).volume(), 45000);
		}
		{
			//attempt to trade
			tradeEngine.trade();
			assertEquals(tradeEngine.getTradeList().size(),2);
			compareTrades(tradeEngine.getTradeList().get(0),t0);
			compareTrades(tradeEngine.getTradeList().get(1),t1);

			assertEquals(orderBooks.bidList().size(), 1);
			compareOrders(orderBooks.bidList().get(0), (o0));
			assertEquals(orderBooks.bidList().get(0).volume(), 100000);

			assertEquals(orderBooks.askList().size(), 2);
			compareOrders(orderBooks.askList().get(0), (o1));
			compareOrders(orderBooks.askList().get(1), (o3));
		}
	}


	private void compareOrders(Order o1, Order o2) {
		assertEquals(o1.time(), o2.time());
		assertEquals(o1.recordType(), o2.recordType());
		assertEquals(o1.price(), o2.price());
		assertEquals(o1.volume(), o2.volume());
		assertEquals(o1.qualifiers(), o2.qualifiers());
		assertEquals(o1.ID(), o2.ID());
		assertEquals(o1.bidAsk(), o2.bidAsk());
	}
	
	private void compareTrades(Trade t1, Trade t2) {
		compareOrders(t1, t2);
		assertEquals(t1.getBid(), t2.getBid());
		assertEquals(t1.getAsk(), t2.getAsk());
	}
}
