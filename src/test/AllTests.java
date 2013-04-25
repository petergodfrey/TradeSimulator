package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestOrder.class, TestOrderBook.class, TestReader.class,
		TestSimulation.class, TestTradeEngine.class })
public class AllTests {

}
