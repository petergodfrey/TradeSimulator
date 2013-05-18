package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

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
		order0 = new Order("00:00:00.000", "ENTER", 8.30, 100, "", new BigInteger("1"), "B");
		order1 = new Order("00:00:01.000", "ENTER", 8.31, 50,  "", new BigInteger("2"), "A");
		order2 = new Order("00:00:02.000", "ENTER", 8.29, 50,  "", new BigInteger("3"), "B");
		order3 = new Order("00:00:03.000", "ENTER", 8.34, 100, "", new BigInteger("4"), "A");
		order4 = new Order("00:00:04.000", "ENTER", 8.31, 40,  "", new BigInteger("5"), "A");
		order5 = new Order("00:00:05.000", "ENTER", 8.31, 50,  "", new BigInteger("6"), "B");


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
		Order del0 = new Order("00:00:06.000", "DELETE", -1, -1, "", new BigInteger("6"), "B");
		orderBooks.processOrder(del0);
		assertSame(orderBooks.bestBidOrder(), order0);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 2);
		Order del1 = new Order("00:00:07.000", "DELETE", -1, -1, "", new BigInteger("4"), "A");
		orderBooks.processOrder(del1);
		assertSame(orderBooks.bestAskOrder(), order1);
		assertEquals(orderBooks.askListSize(), 2);
		assertEquals(orderBooks.bidListSize(), 2);
		Order del2 = new Order("00:00:08.000", "DELETE", -1, -1, "", new BigInteger("1"), "B");
		orderBooks.processOrder(del2);
		assertSame(orderBooks.bestBidOrder(), order2);
		assertEquals(orderBooks.askListSize(), 2);
		assertEquals(orderBooks.bidListSize(), 1);
		
	}

	@Test
	public void testOrderBookAmend() {
		testOrderBookAdd();
		Order amd0 = new Order("00:00:06.000", "AMEND", 7, 50, "", new BigInteger ("6"), "B");
		orderBooks.processOrder(amd0);
		assertSame(orderBooks.bestBidOrder(), order0);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
		Order amd1 = new Order("00:00:07.000", "AMEND", 8.5, 50, "", new BigInteger ("2"), "A");
		orderBooks.processOrder(amd1);
		assertSame(orderBooks.bestAskOrder(), order4);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
		Order amd2 = new Order("00:00:08.000", "AMEND", 8.5, 50, "", new BigInteger ("6"), "B");
		orderBooks.processOrder(amd2);
		assertSame(orderBooks.bestBidOrder(), amd2);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
		Order amd3 = new Order("00:00:09.000", "AMEND", 8.4, 50, "", new BigInteger ("5"), "A");
		orderBooks.processOrder(amd3);
		assertSame(orderBooks.bestAskOrder(), order3);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
		Order amd4 = new Order("00:00:10.000", "AMEND", 8.4, 40, "", new BigInteger ("6"), "B");
		orderBooks.processOrder(amd4);
		assertSame(orderBooks.bestBidOrder(), amd4);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
		Order amd5 = new Order("00:00:11.000", "AMEND", 8.3, 30, "", new BigInteger ("6"), "B");
		orderBooks.processOrder(amd5);
		assertSame(orderBooks.bestBidOrder(), amd5);
		assertEquals(orderBooks.askListSize(), 3);
		assertEquals(orderBooks.bidListSize(), 3);
	}


}

