package simulator.strategy;

import java.util.Random;

import simulator.Order;
import simulator.OrderBooks;

public class RandomStrategy extends AbstractStrategy implements Strategy {
	
	private double OrderGenerationFrequency = 0.1;
	
	private double bidToAskRatio = 0.5;
	
	private int volumeLowerBound = 0, volumeUpperBound = 100;
	
	private double priceLowerBound = 0, priceUpperBound = 100;

	public RandomStrategy(OrderBooks books) {
		super(books);

	}

	@Override
	public String getStrategyName() {
		return "Randomised";
	}

	@Override
	public Order strategise() {
		Random rand = new Random();
		if (rand.nextDouble() > OrderGenerationFrequency) {
			return null;
		}
		String bidAsk = "";
		if (rand.nextDouble() < bidToAskRatio) {
			bidAsk = "B";
		} else {
			bidAsk = "A";
		}
		int volume = rand.nextInt(volumeUpperBound - volumeLowerBound) + volumeLowerBound;
		double price = rand.nextDouble() + (double)(rand.nextInt((int) (priceUpperBound - priceLowerBound)) + priceLowerBound);
		return createOrder("ENTER", price, volume, null, bidAsk);
	}

	public double getPriceUpperBound() {
		return priceUpperBound;
	}

	public void setPriceUpperBound(double priceUpperBound) {
		this.priceUpperBound = priceUpperBound;
	}

	public double getPriceLowerBound() {
		return priceLowerBound;
	}

	public void setPriceLowerBound(double priceLowerBound) {
		this.priceLowerBound = priceLowerBound;
	}

	public int getVolumeUpperBound() {
		return volumeUpperBound;
	}

	public void setVolumeUpperBound(int volumeUpperBound) {
		this.volumeUpperBound = volumeUpperBound;
	}

	public int getVolumeLowerBound() {
		return volumeLowerBound;
	}

	public void setVolumeLowerBound(int volumeLowerBound) {
		this.volumeLowerBound = volumeLowerBound;
	}

	public double getBidToAskRatio() {
		return bidToAskRatio;
	}

	public void setBidToAskRatio(double bidToAskRatio) {
		this.bidToAskRatio = bidToAskRatio;
	}

}
