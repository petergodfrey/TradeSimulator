package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.hamcrest.Factory;
import org.junit.Test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;
import simulator.SignalGenerator;
import simulator.Trade;
import simulator.TradeEngine;
import simulator.strategy.Strategy;

public class TestStrategy {

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
}
