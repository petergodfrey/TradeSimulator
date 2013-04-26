package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import simulator.Factory;
import simulator.Reader;

public class TestReader {
	
	String sample1FilePath = System.getProperty("user.dir") + "/Sample1.csv";
	String sample2FilePath = System.getProperty("user.dir") + "/Sample2.csv";
	String shortSampleFilePath = System.getProperty("user.dir") + "/shortSample.csv";
	
	Reader CSV = null;
	
	Factory f = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		f = new Factory();
	}

	@Test
	public void testCSVChooseFile() {
		try {
			CSV = f.makeReader(sample1FilePath);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		assertEquals(sample1FilePath, CSV.getFilePath());
	}

	@Test
	public void testCSVReadLine() {
		fail("Not yet implemented");
	}
}
