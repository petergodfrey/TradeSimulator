package simulator.strategy;

import java.util.ArrayList;
import java.util.LinkedList;

import simulator.Order;
import simulator.OrderBooks;
import simulator.Trade;
import simulator.TradeEngine;

public class Momentum extends AbstractStrategy implements Strategy {

	TradeEngine tradeEngine;
	private String previousOrderType;

	private Trade tradeReturn;
	private LinkedList<Double> returnsList = new LinkedList<Double>();

	public Momentum(OrderBooks books, TradeEngine tradeEngine) {
		super(books);
		this.tradeEngine   = tradeEngine;
		this.previousOrderType = "NO_ORDER";
	}

	@Override
	public Order strategise() {

		//for times when it is not appropriate to create an order,
		//calcAvgReturns returns 0
		double averageReturn = calcAvgReturns();

		if (averageReturn > 0 && !previousOrderType.equals("B")) {
			previousOrderType = "B";
			return createOrder("ENTER", books.bestBidPrice() + 0.01, books.bestAskOrder().volume(), null, "B");
		} else if (averageReturn < 0 && !previousOrderType.equals("A")) {
			previousOrderType = "A";
			return createOrder("ENTER", books.bestAskPrice() - 0.01, books.bestBidOrder().volume(), null, "A");
		}

		return Order.NO_ORDER;
	}

	@Override
	public String getStrategyName() {
		return "Momentum";
	}

	//currently public function of testing
	public double computeReturn() {

		if (tradeEngine.getTradeList().size() == 0) {
			//if Tradelist == 0, can't compute any returns
			return 0;
		}
		if (tradeReturn == null) {
			//occurs once only, first trade only
			tradeReturn = tradeEngine.getTradeList().get(0);
			return 0;
		}
		//make sure a new trade has occurred before getting the return
		if (tradeEngine.getTradeList().get(0) == tradeReturn) {
			return 0;
		}
		{
			double returns =
					(tradeEngine.getTradeList().get(0).price() - tradeReturn.price())
					/tradeReturn.price();

			//rounds to 5 decimal places
			returns = Math.round(returns*10000);
			returns = returns/10000;

			tradeReturn = tradeEngine.getTradeList().get(0);//set current trade as the previous trade now
			returnsList.add(0, returns);//add newly calculated return into list
			return returns;
		}
	}

	private double calcAvgReturns() {
		double returns = avgReturns();
		computeReturn();
		return returns;
	}
	
	//curently public function for testing
	public double avgReturns() {
		
		int lookBackPeriod = 10;

		if (returnsList.size() % lookBackPeriod != 0 || returnsList.size() == 0) {
			//if there haven't been enough returns to calculate the next set of avgReturns
			//or if there is nothing in the list
			return 0;
		}
		{
			double avgReturns = 0;
			for (int i = 0; i < lookBackPeriod; i++) {
				avgReturns += returnsList.get(i);
			}
			avgReturns /= lookBackPeriod;

			return avgReturns;
		}
	}

	@Override
	public void reset() {
		super.reset();
		previousOrderType = "NO_ORDER";
		tradeReturn = null;
		returnsList = new LinkedList<Double>();
	}

}
