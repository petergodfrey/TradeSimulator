package test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import simulator.Factory;
import simulator.Order;
import simulator.OrderBooks;
import simulator.TradeEngine;
import simulator.strategy.Momentum;

public class TestStrategy {
	
	Factory f;
	OrderBooks orderBooks;
	TradeEngine tradeEngine;
	Order o0, o1, o2, o3, o4, o5 ,o6 ,o7, o8 ,o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21;

	@Test
	public void testCalculateAverageReturn() {
		double sum = 0;
		int    counter = 0;
		double currentPrice;
		double previousPrice;
		//ArrayList<Trade> trades = tradeEngine.getTradeList();
		double list[] = {2.5, 2.45, 2.35, 2.2, 2.5, 2.45, 2.35, 2.2, 2.1, 2, 1.8};
		for (int i = 1; i < 11; i++) {
			if (11 - i - 1 >= 0) {
			    currentPrice  = list[11 - i];
				previousPrice = list[11 - i -1];
				sum += (currentPrice - previousPrice) / previousPrice;
				counter++;
		    }
		}
		System.out.println((double) sum/counter);

	}
	
	@Test
	public void testReturnsCalculations() {
		f = new Factory();
		orderBooks = f.makeOrderBooks();
		tradeEngine = f.makeTradeEngine();
		Momentum strat = (Momentum) f.makeMomentumStrategy();
		
		o0 = new Order("11:00:00.000", "ENTER", 2.5, 1, "", new BigInteger ("1"), "A");
		o1 = new Order("11:00:00.000", "ENTER", 2.5, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o0);
		orderBooks.processOrder(o1);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), (double) 0));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.5));
		
		o2 = new Order("11:00:00.000", "ENTER", 2.45, 1, "", new BigInteger ("1"), "A");
		o3 = new Order("11:00:00.000", "ENTER", 2.45, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o2);
		orderBooks.processOrder(o3);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.02));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.45));
		
		o4 = new Order("11:00:00.000", "ENTER", 2.35, 1, "", new BigInteger ("1"), "A");
		o5 = new Order("11:00:00.000", "ENTER", 2.35, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o4);
		orderBooks.processOrder(o5);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.04082));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.35));
		
		o6 = new Order("11:00:00.000", "ENTER", 2.2, 1, "", new BigInteger ("1"), "A");
		o7 = new Order("11:00:00.000", "ENTER", 2.2, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o6);
		orderBooks.processOrder(o7);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.06383));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.2));
		
		o8 = new Order("11:00:00.000", "ENTER", 2.5, 1, "", new BigInteger ("1"), "A");
		o9 = new Order("11:00:00.000", "ENTER", 2.5, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o8);
		orderBooks.processOrder(o9);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), 0.136364));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.5));
		
		o10 = new Order("11:00:00.000", "ENTER", 2.45, 1, "", new BigInteger ("1"), "A");
		o11 = new Order("11:00:00.000", "ENTER", 2.45, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o10);
		orderBooks.processOrder(o11);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.02));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.45));
		
		o12 = new Order("11:00:00.000", "ENTER", 2.35, 1, "", new BigInteger ("1"), "A");
		o13 = new Order("11:00:00.000", "ENTER", 2.35, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o12);
		orderBooks.processOrder(o13);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.04082));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.35));
		
		o14 = new Order("11:00:00.000", "ENTER", 2.2, 1, "", new BigInteger ("1"), "A");
		o15 = new Order("11:00:00.000", "ENTER", 2.2, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o14);
		orderBooks.processOrder(o15);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.06383));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.2));
		
		o16 = new Order("11:00:00.000", "ENTER", 2.1, 1, "", new BigInteger ("1"), "A");
		o17 = new Order("11:00:00.000", "ENTER", 2.1, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o16);
		orderBooks.processOrder(o17);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.04545));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.1));
		
		o18 = new Order("11:00:00.000", "ENTER", 2.0, 1, "", new BigInteger ("1"), "A");
		o19 = new Order("11:00:00.000", "ENTER", 2.0, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o18);
		orderBooks.processOrder(o19);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.04762));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 2.0));
		
		o20 = new Order("11:00:00.000", "ENTER", 1.8, 1, "", new BigInteger ("1"), "A");
		o21 = new Order("11:00:00.000", "ENTER", 1.8, 1, "", new BigInteger ("1"), "B");

		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		orderBooks.processOrder(o20);
		orderBooks.processOrder(o21);
		tradeEngine.trade();
		assertTrue(OrderBooks.compareDoubleEquals(strat.computeReturn(), -0.1));
		assertTrue(OrderBooks.compareDoubleEquals(strat.avgReturns(), -0.0306));
		
		assertEquals(orderBooks.askListSize(), 0);
		assertEquals(orderBooks.bidListSize(), 0);
		assertTrue(OrderBooks.compareDoubleEquals(tradeEngine.getTradeList().get(0).price(), 1.8));
		
		
	}
}
