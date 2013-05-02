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
import simulator.Trade;
import simulator.TradeEngine;
import simulator.Strategy.Strategy;

public class TestTradeEngine {
	
	OrderBooks books;
	Factory f;
	Reader CSV;
	Strategy strat;
	SignalGenerator SG;
	TradeEngine TE;
	
	Order order0, order1, order2, order3;
	
	
	String sample1FilePath = System.getProperty("user.dir") + "/Sample1.csv";
	String sample2FilePath = System.getProperty("user.dir") + "/Sample2.csv";
	String shortSampleFilePath = System.getProperty("user.dir") + "/shortSample.csv";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		f = new Factory();
		books = f.makeOrderBooks();
		TE = f.makeTradeEngine();
		
		assertEquals(books.askListSize(), 0);
		assertEquals(books.bidListSize(), 0);
		assertEquals(TE.getTradeList().size(), 0);
	}
	

	@Test
	public void testCorrectTradeAttributes() {
		//tests for the correct attributes of a trade
		//fail("Not yet implemented");
	}

	@Test
	public void testTradeEqualVolumeEqualPrice() {
		//tests to see if the trades list: isn't missing any, correctly captures strategy trades
		//anything else that you can think of

		order0 = new Order("00:00:00.000", "ENTER", 8.31, 50, "", 1, "B");
		order1 = new Order("00:00:01.000", "ENTER", 8.31, 50, "", 2, "A");
		books.processOrder(order0);
		assertEquals(books.bidListSize(), 1);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 0);
		books.processOrder(order1);
		assertEquals(books.askListSize(), 1);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 1);
		compareTrade(TE.getTradeList().get(0), order0, order1, 8.31, 50, "00:00:01.000");
		assertEquals(books.askListSize(), 0);
		assertEquals(books.bidListSize(), 0);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 1);
		assertEquals(books.askListSize(), 0);
		assertEquals(books.bidListSize(), 0);

	}
	
	@Test
	public void testTradeEqualVolumeUnequalPrice() {
		order0 = new Order("00:00:00.000", "ENTER", 8.34, 50, "", 1, "B");
		order1 = new Order("00:00:01.000", "ENTER", 8.30, 50, "", 2, "A");
		books.processOrder(order0);
		assertEquals(books.bidListSize(), 1);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 0);
		books.processOrder(order1);
		assertEquals(books.askListSize(), 1);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 1);
		compareTrade(TE.getTradeList().get(0), order0, order1 , 8.30, 50, "00:00:01.000");
		assertEquals(books.askListSize(), 0);
		assertEquals(books.bidListSize(), 0);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 1);
		assertEquals(books.askListSize(), 0);
		assertEquals(books.bidListSize(), 0);
	}
	
	@Test
	public void testUnequalVolumeEqualPrice() {
		order0 = new Order("00:00:00.000", "ENTER", 8.34, 100, "", 1, "B");
		order1 = new Order("00:00:01.000", "ENTER", 8.34, 50, "", 2, "A");
		order2 = new Order("00:00:02.000", "ENTER", 8.34, 50, "", 3, "A");
		books.processOrder(order0);
		assertEquals(books.bidListSize(), 1);
		assertEquals(books.askListSize(), 0);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 0);
		books.processOrder(order1);
		assertEquals(books.askListSize(), 1);
		books.processOrder(order2);
		assertEquals(books.askListSize(), 2);
		TE.trade();
		assertEquals(TE.getTradeList().size(), 2);
		compareTrade(TE.getTradeList().get(0), order0, order2, 8.34, 50, "00:00:02.000");
		compareTrade(TE.getTradeList().get(1), order0, order1, 8.34, 50, "00:00:02.000");
		
		assertEquals(books.bidListSize(), 0);
		assertEquals(books.askListSize(), 0);
	}
	
	@Test
	public void testUnequalVolumeUnequalPrice() {
		order0 = new Order("00:00:00.000", "ENTER", 8.34, 150, "", 1, "B");
		order1 = new Order("00:00:01.000", "ENTER", 8.29, 50, "", 2, "A");
		order2 = new Order("00:00:02.000", "ENTER", 8.35, 50, "", 3, "A");
		order3 = new Order("00:00:03.000", "ENTER", 8.30, 150, "", 4, "A");
		books.processOrder(order0);
		assertEquals(books.bidListSize(), 1);
		assertEquals(books.askListSize(), 0);
		books.processOrder(order1);
		assertEquals(books.bidListSize(), 1);
		assertEquals(books.askListSize(), 1);
		books.processOrder(order2);
		assertEquals(books.bidListSize(), 1);
		assertEquals(books.askListSize(), 2);
		books.processOrder(order3);
		assertEquals(books.bidListSize(), 1);
		assertEquals(books.askListSize(), 3);
		TE.trade();
		assertEquals(books.bidListSize(), 0);
		assertEquals(books.askListSize(), 2);
		assertEquals(TE.getTradeList().size(), 2);
		compareTrade(TE.getTradeList().get(0), order0, order3, 8.3, 50, "00:00:03.000");
		compareTrade(TE.getTradeList().get(1), order0, order1, 8.3, 50, "00:00:03.000");
	}
	
	private void compareTrade(Trade t, Order bid, Order ask, Double price, int volume, String time) {

		assertTrue(OrderBooks.compareDoubleEquals(t.price(), price));
		assertEquals(t.volume(), volume);
		assertEquals(t.time(), time);
	}
}
