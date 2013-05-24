package test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import simulator.Factory;
import simulator.Order;

public class TestOrder {
	
	String indexes = "#Instrument,Date,Time,Record Type,Price,Volume,Undisclosed Volume,Value,Qualifiers,Trans ID,Bid ID,Ask ID,Bid/Ask,Entry Time,Old Price,Old Volume,Buyer Broker ID,Seller Broker ID";

	Order order0, order1, order2, order3, order4, order5, order6, order7, order8, order9, order10, order11, order12, order13;
	
	String str0, str1, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13;
	
	Factory f;

	@Before
	public void setUp() throws Exception {
		
		f = new Factory();
		f.setCSVColumns(indexes);

		str0 = "CBA,19991231,00:00:00.000,ENTER,8.30,100,,,,,1,,B,,,,,";
		str1 = "CBA,19991231,00:00:01.000,ENTER,8.31,050,,,,,,2,A,,,,,";
		str2 = "CBA,19991231,00:00:02.000,ENTER,8.29,050,,,,,3,,B,,,,,";
		str3 = "CBA,19991231,00:00:03.000,ENTER,8.34,100,,,,,,4,A,,,,,";
		str4 = "CBA,19991231,00:00:04.000,ENTER,8.31,040,,,,,,5,A,,,,,";
		str5 = "CBA,19991231,00:00:05.000,ENTER,8.31,050,,,,,6,,B,,,,,";
		
		str6 = "CBA,19991231,00:00:06.000,AMEND,9.31,150,,,,,1,,B,,,,,";
		str7 = "CBA,19991231,00:00:07.000,AMEND,9.31,200,,,,,,2,A,,,,,";
		str8 = "CBA,19991231,00:00:08.000,AMEND,9.31,250,,,,,3,,B,,,,,";
		str9 = "CBA,19991231,00:00:09.000,AMEND,9.31,030,,,,,,4,A,,,,,";
		
		str10 = "CBA,19991231,00:00:10.000,DELETE,,,,,,,1,,B,,,,,";
		str11 = "CBA,19991231,00:00:11.000,DELETE,,,,,,,,2,A,,,,,";
		str12 = "CBA,19991231,00:00:12.000,DELETE,,,,,,,3,,B,,,,,";
		str13 = "CBA,19991231,00:00:13.000,DELETE,,,,,,,,4,A,,,,,";

		order0 = new Order("00:00:00.000", "ENTER", 8.30, 100, "", new BigInteger("1"), "B");
		order1 = new Order("00:00:01.000", "ENTER", 8.31, 50,  "", new BigInteger("2"), "A");
		order2 = new Order("00:00:02.000", "ENTER", 8.29, 50,  "", new BigInteger("3"), "B");
		order3 = new Order("00:00:03.000", "ENTER", 8.34, 100, "", new BigInteger("4"), "A");
		order4 = new Order("00:00:04.000", "ENTER", 8.31, 40,  "", new BigInteger("5"), "A");
		order5 = new Order("00:00:05.000", "ENTER", 8.31, 50,  "", new BigInteger("6"), "B");
		
		order6 = new Order("00:00:06.000", "AMEND", 9.31, 150,  "", new BigInteger("1"), "B");
		order7 = new Order("00:00:07.000", "AMEND", 9.31, 200,  "", new BigInteger("2"), "A");
		order8 = new Order("00:00:08.000", "AMEND", 9.31, 250,  "", new BigInteger("3"), "B");
		order9 = new Order("00:00:09.000", "AMEND", 9.31, 30,   "", new BigInteger("4"), "A");
		
		order10 = new Order("00:00:10.000", "DELETE", -1 , -1,  "", new BigInteger("1"), "B");
		order11 = new Order("00:00:11.000", "DELETE", -1 , -1,  "", new BigInteger("2"), "A");
		order12 = new Order("00:00:12.000", "DELETE", -1 , -1,  "", new BigInteger("3"), "B");
		order13 = new Order("00:00:13.000", "DELETE", -1 , -1,  "", new BigInteger("4"), "A");

	}
	
	@Test
	public void testOrderCreationFromCSV() {
		Order o0 = f.makeOrderFromCSV(str0);
		Order o1 = f.makeOrderFromCSV(str1);
		Order o2 = f.makeOrderFromCSV(str2);
		Order o3 = f.makeOrderFromCSV(str3);
		Order o4 = f.makeOrderFromCSV(str4);
		Order o5 = f.makeOrderFromCSV(str5);
		Order o6 = f.makeOrderFromCSV(str6);
		Order o7 = f.makeOrderFromCSV(str7);
		Order o8 = f.makeOrderFromCSV(str8);
		Order o9 = f.makeOrderFromCSV(str9);
		Order o10 = f.makeOrderFromCSV(str10);
		Order o11 = f.makeOrderFromCSV(str11);
		Order o12 = f.makeOrderFromCSV(str12);
		Order o13 = f.makeOrderFromCSV(str13);
		compareOrders(o0, order0);
		compareOrders(o1, order1);
		compareOrders(o2, order2);
		compareOrders(o3, order3);
		compareOrders(o4, order4);
		compareOrders(o5, order5);
		compareOrders(o6, order6);
		compareOrders(o7, order7);
		compareOrders(o8, order8);
		compareOrders(o9, order9);
		compareOrders(o10, order10);
		compareOrders(o11, order11);
		compareOrders(o12, order12);
		compareOrders(o13, order13);

		
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
	
	@Test
	public void testOrderGetters() {
		assertEquals("00:00:00.000", order0.time());
		assertEquals("ENTER", order0.recordType());
		assertEquals((Double)(8.30), order0.price());
		assertEquals(100, order0.volume());
		assertEquals("", order0.qualifiers());
		assertEquals(new BigInteger("1"), order0.ID());
		assertEquals("B", order0.bidAsk());	
		
		assertEquals("00:00:01.000", order1.time());
		assertEquals("ENTER", order1.recordType());
		assertEquals((Double)(8.31), order1.price());
		assertEquals(50, order1.volume());
		assertEquals("", order1.qualifiers());
		assertEquals(new BigInteger("2"), order1.ID());
		assertEquals("A", order1.bidAsk());	
		
		assertEquals("00:00:02.000", order2.time());
		assertEquals("ENTER", order2.recordType());
		assertEquals((Double)(8.29), order2.price());
		assertEquals(50, order2.volume());
		assertEquals("", order2.qualifiers());
		assertEquals(new BigInteger("3"), order2.ID());
		assertEquals("B", order2.bidAsk());	
		
		assertEquals("00:00:03.000", order3.time());
		assertEquals("ENTER", order3.recordType());
		assertEquals((Double)(8.34), order3.price());
		assertEquals(100, order3.volume());
		assertEquals("", order3.qualifiers());
		assertEquals(new BigInteger("4"), order3.ID());
		assertEquals("A", order3.bidAsk());	
		
		assertEquals("00:00:04.000", order4.time());
		assertEquals("ENTER", order4.recordType());
		assertEquals((Double)(8.31), order4.price());
		assertEquals(40, order4.volume());
		assertEquals("", order4.qualifiers());
		assertEquals(new BigInteger("5"), order4.ID());
		assertEquals("A", order4.bidAsk());	
		
		assertEquals("00:00:05.000", order5.time());
		assertEquals("ENTER", order5.recordType());
		assertEquals((Double)(8.31), order5.price());
		assertEquals(50, order5.volume());
		assertEquals("", order5.qualifiers());
		assertEquals(new BigInteger("6"), order5.ID());
		assertEquals("B", order5.bidAsk());	
	}

}
