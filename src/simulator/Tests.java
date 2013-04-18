package simulator;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests {
	
	String sampleFilePath;
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
		sampleFilePath = new String("F:/SENG3011/Codes/TradeSimulator/sample1.csv");
	}
		
	@Test
	public void testCSVChooseFile() {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(sampleFilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		assertEquals(sampleFilePath, CSV.getFilePath());
	}
	
	@Test
	public void testCSVReadLine() {
		
	}

	@Test
	public void testOrderGeneration() {
		Order order = new Order("instrument", "date", "time", "recordType", 0.00, 0.00, "qualifiers", 1234, 23445, 123, "bidAsk");
		assertEquals("instrument", order.instrument());
		assertEquals("date", order.date());
		assertEquals("time", order.time());
		assertEquals("recordType", order.recordType());
		assertEquals(new Double(0.00), order.price());
		assertEquals(new Double(0.00), order.volume());
		assertEquals("qualifiers", order.qualifiers());
		assertEquals(1234, order.transactionID());
		assertEquals(23445, order.bidID());
		assertEquals(123, order.askID());
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
		Order bid1 = new Order("CBA", "20130304", "00:00:00.000", "ENTER", 67.76, 1959.0 , new String(), new Long(0), new Long("6239925033925459786"), new Long(0), "B");
		Order bid2 = new Order("CBA", "20130304", "00:00:00.000", "ENTER", 67.720, 15,0,1015.8,,0,6239925033924850752,,B,,,,203);
		OrderBooks b = factory.makeOrderBooks(); 
		b.processOrder(bid1);
		assertEquals(1, b.bidListSize());
		
		assertEquals(bid, b.bestBidOrder());
	}
	
	@Test
	public void testOrderBookGetAsk() {		
		OrderBooks b = factory.makeOrderBooks();
		Order ask = new Order("CBA", "20130304" ,"00:00:00.000","ENTER", 67.900, 409,new String(),new Long(0), new Long(0), new Long("6238329642550435411"), "A");
		b.processOrder(ask);
		assertEquals(1, b.askListSize());
		
		assertEquals(ask, b.bestAskOrder());
	}
	
	@Test
	public void testOrderBookAdd() {
		Order bid = new Order("CBA", "20130304", "00:00:00.000", "ENTER", 67.76, 1959.0 , new String(), new Long(0), new Long("6239925033925459786"), new Long(0), "B");
		OrderBooks b = factory.makeOrderBooks(); 
		b.processOrder(bid);
		assertEquals(1, b.bidListSize());
		assertEquals(bid, b.bestBidOrder());
		
		Order ask = new Order("CBA", "20130304" ,"00:00:00.000","ENTER", 67.900, 409,new String(),new Long(0), new Long(0), new Long("6238329642550435411"), "A");
		b.processOrder(ask);
		assertEquals(1, b.askListSize());
		assertEquals(ask, b.bestAskOrder());
	}
	
	@Test
	public void testOrderBookDelete() {
		Order bid = new Order("CBA", "20130304", "00:00:00.000", "ENTER", 67.76, 1959.0 , new String(), new Long(0), new Long("6239925033925459786"), new Long(0), "B");
		OrderBooks b = factory.makeOrderBooks(); 
		b.processOrder(bid);
		assertEquals(1, b.bidListSize());
		assertEquals(bid, b.bestBidOrder());
		Order bidD = new Order("CBA", "20130304", "00:00:00.000", "DELETE", 67.76, 1959.0 , new String(), new Long(0), new Long("6239925033925459786"), new Long(0), "B");
		b.processOrder(bidD);
		assertEquals(0, b.bidListSize());
		
		Order ask = new Order("CBA", "20130304" ,"00:00:00.000","ENTER", 67.900, 409,new String(),new Long(0), new Long(0), new Long("6238329642550435411"), "A");
		b.processOrder(ask);
		assertEquals(1, b.askListSize());
		assertEquals(ask, b.bestAskOrder());
		Order askD = new Order("CBA", "20130304" ,"00:00:00.000","DELETE", 67.900, 409,new String(),new Long(0), new Long(0), new Long("6238329642550435411"), "A");	
		b.processOrder(askD);
		assertEquals(0, b.askListSize());
	}
	
	@Test
	public void testOrderBookAmend() {
		// unimplemented yet
	}
	
	@Test
	public void testOrderBookTrade() {
		// unimplemented yet
	}
	
	@Test
	public void testOrderBookOffTrade() {
		// unimplemented yet
	}
	
	@Test
	public void testOrderBookCancelTrade() {
		// unimplemented yet
	}
	
	@Test
	public void testGeneratorGetTimeStamp() {

	}

	@Test
	public void testGeneratorGetRecordType() {

	}

	@Test
	public void testGeneratorGetPrice() {

	}

	@Test
	public void testGeneratorGetVolume() {

	}

	@Test
	public void testGeneratorGetBidAsk() {

	}
	
	@Test
	public void testGeneratorGetQualifier() {

	}
}
