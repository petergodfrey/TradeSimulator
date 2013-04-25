package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTradeEngine {
	
	String sample1FilePath = System.getProperty("user.dir") + "/Sample1.csv";
	String sample2FilePath = System.getProperty("user.dir") + "/Sample2.csv";
	String shortSampleFilePath = System.getProperty("user.dir") + "/shortSample.csv";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCorrectTradeAttributes() {
		//tests for the correct attributes of a trade
		fail("Not yet implemented");
	}

	@Test
	public void testTradeList() {
		//tests to see if the trades list: isn't missing any, correctly captures strategy trades
		//anything else that you can think of
		fail("Not yet implemented");
	}
}
