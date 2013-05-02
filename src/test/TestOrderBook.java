package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import simulator.Factory;
import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;

public class TestOrderBook {

	String sample1FilePath = System.getProperty("user.dir") + "/Sample1.csv";
	String sample2FilePath = System.getProperty("user.dir") + "/Sample2.csv";
	String shortSampleFilePath = System.getProperty("user.dir") + "/shortSample.csv";
	OrderBooks orderBooks;
	Factory f;

	Order order0;
	Order order1;
	Order order2;
	Order order3;
	Order order4;
	Order order5;

	@Before
	public void setUp() throws Exception {


		f = new Factory();
		orderBooks = f.makeOrderBooks();
		order0 = new Order("00:00:00.000", "ENTER", 8.30, 100, "", 1, "B");
		order1 = new Order("00:00:01.000", "ENTER", 8.31, 50,  "", 2, "A");
		order2 = new Order("00:00:02.000", "ENTER", 8.29, 50,  "", 3, "B");
		order3 = new Order("00:00:03.000", "ENTER", 8.34, 100, "", 4, "A");
		order4 = new Order("00:00:04.000", "ENTER", 8.31, 40,  "", 5, "A");
		order5 = new Order("00:00:05.000", "ENTER", 8.31, 50,  "", 6, "B");


	}


	@Test
	public void testOrderBookGetBid() {
		orderBooks.processOrder(order0);
		assertEquals(1, orderBooks.bidListSize());
		assertEquals(order0, orderBooks.bestBidOrder());
		orderBooks.processOrder(order2);
		assertEquals(2, orderBooks.bidListSize());
		assertEquals(order0, orderBooks.bestBidOrder());
		orderBooks.processOrder(order5);
		assertEquals(3, orderBooks.bidListSize());
		assertEquals(order5, orderBooks.bestBidOrder());
	}

	@Test
	public void testOrderBookGetAsk() {		
		orderBooks.processOrder(order1);
		assertEquals(1, orderBooks.askListSize());		
		assertEquals(order1, orderBooks.bestAskOrder());
		orderBooks.processOrder(order3);
		assertEquals(2, orderBooks.askListSize());		
		assertEquals(order1, orderBooks.bestAskOrder());
		orderBooks.processOrder(order4); 
		assertEquals(3, orderBooks.askListSize());		
		assertEquals(order1, orderBooks.bestAskOrder());
	}

	@Test
	public void testSpread() {
		orderBooks.processOrder(order0);
		orderBooks.processOrder(order1);
		orderBooks.processOrder(order2);
		orderBooks.processOrder(order3);
		orderBooks.processOrder(order4);
		//double comparison errors
		assertTrue(OrderBooks.compareDoubleEquals(orderBooks.spread(), 0.0));
		orderBooks.processOrder(order5);
		assertTrue(orderBooks.spread() >= 0);
	}

	@Test
	public void testOrderBookAdd() {

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(order0);
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 1);
		orderBooks.processOrder(order1);
		assertEquals(orderBooks.askListSize(), 1);
		assertEquals(orderBooks.bidListSize(), 1);
		orderBooks.processOrder(order2);
		assertEquals(orderBooks.askListSize(), 1);
		assertEquals(orderBooks.bidListSize(), 2);
		orderBooks.processOrder(order3);
		assertEquals(orderBooks.askListSize(), 2);
		assertEquals(orderBooks.bidListSize(), 2);
		orderBooks.processOrder(order4);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 2);
		orderBooks.processOrder(order5);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
	}

	//@Test
	public void testTimeConversion() {
		//generates every millisecond of the day and checks correct conversion
		int milliTime = 0;
		String time = "00:00:00.000";
		for (int l = 0; l < 24; l++) {
			System.out.println(time);
			for (int k = 0; k < 60; k++) {
				for (int j = 0; j < 60; j++) {
					for (int i = 0; i < 1000; i++, milliTime++) {
						time = l + ":" + k + ":" + j + "." + String.format("%03d", i);
						assertEquals(OrderBooks.convertTimeToMilliseconds(time), milliTime);
					}
				}
			}
		}
	}

	@Test
	public void testOrderBookDelete() {
		testOrderBookAdd();
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
		assertSame(orderBooks.bestBidOrder(), order5);
		Order del0 = new Order("00:00:06.000", "DELETE", -1, -1, "", 6, "B");
		orderBooks.processOrder(del0);
		assertSame(orderBooks.bestBidOrder(), order0);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 2);
		Order del1 = new Order("00:00:07.000", "DELETE", -1, -1, "", 4, "A");
		orderBooks.processOrder(del1);
		assertSame(orderBooks.bestAskOrder(), order1);
		assertEquals(orderBooks.askListSize(), 2);
		assertEquals(orderBooks.bidListSize(), 2);
		Order del2 = new Order("00:00:08.000", "DELETE", -1, -1, "", 1, "B");
		orderBooks.processOrder(del2);
		//assertSame(orderBooks.bestBidOrder(), order0);
		//assertEquals(orderBooks.askListSize(), 2);
		//assertEquals(orderBooks.bidListSize(), 1);
		

		/*Order bid = new Order("00:00:00.000", "ENTER", 67.76, 1959 , new String(), new Long("6239925033925459786"), "B");
		OrderBooks b = f.makeOrderBooks(); 
		b.processOrder(bid);
		assertEquals(1, b.bidListSize());
		assertEquals(bid, b.bestBidOrder());
		Order bidD = new Order("00:00:00.000", "DELETE", 67.76, 1959 , new String(), new Long("6239925033925459786"), "B");
		b.processOrder(bidD);
		assertEquals(0, b.bidListSize());

		Order ask = new Order("00:00:00.000","ENTER", 67.900, 409,new String(), new Long("6238329642550435411"), "A");
		b.processOrder(ask);
		assertEquals(1, b.askListSize());
		assertEquals(ask, b.bestAskOrder());
		Order askD = new Order("00:00:00.000","DELETE", 67.900, 409,new String(), new Long("6238329642550435411"), "A");	
		b.processOrder(askD);
		assertEquals(0, b.askListSize());*/
	}

	@Test
	public void testOrderBookAmend() {
		// related functions are implementing
		fail("Not yet implemented");
	}


}

