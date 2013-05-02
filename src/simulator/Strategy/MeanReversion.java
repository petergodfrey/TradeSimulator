package simulator.Strategy;

import java.util.List;

import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;

public class MeanReversion extends AbstractStrategy implements Strategy {

	private Double mean; 
	
	public MeanReversion(OrderBooks books) {
		super(books);
	}

	public void calculateMean() {
		// pass the file path as a parameter to makeMeanReversion() in Factoroy
		// and modify the filePath for previous days 
		//this.mean = (highest + lowest) / 2;		
	}	
	
	@Override
	public Order strategise() {
		// find the highest & lowest price in previous files 
		// calculate the mean and set it as criteria
		Order o = null;
		
		// if the price is lower than mean, buy order
		// if the price is greater than mean, sell order
		return null;
	}

	@Override
	public String getStrategyName() {
		return "MeanReversion";
	}

}
