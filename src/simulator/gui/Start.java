package simulator.gui;

import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import simulator.Evaluator;
import simulator.Factory;
import simulator.Order;
import simulator.OrderBooks;
import simulator.Reader;
import simulator.SignalGenerator;
import simulator.Trade;
import simulator.TradeEngine;
import simulator.strategy.*;

public class Start {
	
	public static Reader getFilePath (String pathInput, Factory factory) {
		Reader CSV = null;
		try {
			CSV = factory.makeReader(pathInput);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
                    "Invalid filepath, enter the correct path.",
                    "getFilePath error",
                    JOptionPane.ERROR_MESSAGE);
		}
		return CSV;
	}
	
	public static Strategy selectStrategy(String straInput, Factory factory) {
		
		Strategy strat = null;
		
		if (straInput == "Mean Reversion") {
			strat =  factory.makeMeanReversionStrategy();
		} else if (straInput == "Momentum"){
			strat = factory.makeMomentumStrategy();
		} else if (straInput == "Dumb") {
			strat = factory.makeDumbStrategy();
		} else if (straInput == "Random") {
			strat = factory.makeRandomStrategy();
		} else if (straInput == "Null") {
			strat = factory.makeNullStrategy();
		}
		return strat;
	}

	public static Strategy selectComparison(String straCompare, Factory factory) {
		
		Strategy compare = null;
		
		if (straCompare == "Mean Reversion") {
			compare =  factory.makeMeanReversionStrategy();
		} else if (straCompare == "Momentum"){
			compare = factory.makeMomentumStrategy();
		} else if (straCompare == "Dumb") {
			compare = factory.makeDumbStrategy();
		} else if (straCompare == "Random") {
			compare = factory.makeRandomStrategy();
		} else if (straCompare == "Null") {
			compare = factory.makeNullStrategy();
		}
		return compare;
	}
	
	
	public static double runSimulation(Reader CSV, Strategy strat, Factory factory) {
		Double profit = 0.0;
		//cannot run simulation if there is no CSV chosen
		if (CSV == null) {
			JOptionPane.showMessageDialog(new JFrame(),
                    "A CSV file has not been selected, cannot run simulation",
                    "runSimulation error",
                    JOptionPane.ERROR_MESSAGE);
			return profit;//exit function early
		}

		SignalGenerator signalGenerator = null;
		try {
			CSV = factory.makeReader(CSV.getFilePath());

			signalGenerator = new SignalGenerator(CSV, strat, factory);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Error in reading CSV file, exiting simulation",
                    "runSimulation error",
                    JOptionPane.ERROR_MESSAGE);
			return profit;
		}

		OrderBooks  orderBooks  = factory.makeOrderBooks();
		TradeEngine tradeEngine = factory.makeTradeEngine();
		//ensure each simulation run begins with empty orderbooks and trade lists
		//ensures successive simulations are unaffected
		orderBooks.resetOrderBooks();
		tradeEngine.resetTradeEngine();
		
		//reset any strategy attributes after each simulation
		strat.reset();

		Order o;
		
		int progressCounter = 0;
		
		int OnePercentLines = CSV.getFileSize()/100;
		
		while ((o = signalGenerator.advance()) != null) {
			//one iteration equals one order being processed and traded
			orderBooks.processOrder(o);
			tradeEngine.trade();
			if (progressCounter == OnePercentLines) {
				updateProgressBar(CSV);
				progressCounter = 0;
			}
			progressCounter++;
		}
		updateProgressBar(CSV);
		
		Evaluator eval = new Evaluator(strat, tradeEngine, orderBooks);
		List<Trade> strategyTrades = eval.filterStrategyTrades();
		if (strategyTrades.size() == 0) {

		} else {
			displayEvaluation(strategyTrades);
			profit = new Double (eval.getProfit());
			Main.lbProfitResult.setText("$ " + String.format("%.2f", profit));
		}
		Main.lblDisplayTotalBuy.setText(eval.getTotalBuy().toString());
		Main.lblDisplayTotalSell.setText(eval.getTotalSell().toString());
		Double returns = (((double) eval.getTotalSell() - (double) eval.getTotalBuy()) / (double) eval.getTotalBuy()) * 100;
		Main.lblDisplayRetuns.setText(String.format("%.2f", returns) + " %");
		Chart.drawChart(tradeEngine.getTradeList());
		return profit;
	}
	
	static public void runComparison(Reader CSV, Strategy compare, Strategy strat, Factory factory, double result) {
		double profit = 0;
		//cannot run simulation if there is no CSV chosen
		if (CSV == null) {
			JOptionPane.showMessageDialog(new JFrame(),
					"A CSV file has not been selected, cannot run simulation",
                    "runComparison error",
                    JOptionPane.ERROR_MESSAGE);
			return;//exit function early
		}
		
		if (strat == null) {
			JOptionPane.showMessageDialog(new JFrame(),
					"There's no source to compare, cannot run simulation",
                    "runComparison error",
                    JOptionPane.ERROR_MESSAGE);
			return;//exit function early
		}
		SignalGenerator signalGenerator = null;
		try {
			CSV = factory.makeReader(CSV.getFilePath());
			signalGenerator = new SignalGenerator(CSV, compare, factory);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Error in reading CSV file, exiting simulation",
                    "runComparison error",
                    JOptionPane.ERROR_MESSAGE);
			return;
		}

		OrderBooks  orderBooks  = factory.makeOrderBooks();
		TradeEngine tradeEngine = factory.makeTradeEngine();
		//ensure each simulation run begins with empty orderbooks and trade lists
		//ensures successive simulations are unaffected
		orderBooks.resetOrderBooks();
		tradeEngine.resetTradeEngine();
		
		//reset any strategy attributes after each simulation
		compare.reset();

		Order o;
		
		int progressCounter = 0;
		
		int OnePercentLines = CSV.getFileSize()/100;
		
		while ((o = signalGenerator.advance()) != null) {
			//one iteration equals one order being processed and traded
			orderBooks.processOrder(o);
			tradeEngine.trade();
			if (progressCounter == OnePercentLines) {
				updateProgressBar(CSV);
				progressCounter = 0;
			}
			progressCounter++;
		}
		updateProgressBar(CSV);
		
		while ((o = signalGenerator.advance()) != null) {
			//one iteration equals one order being processed and traded
			orderBooks.processOrder(o);
			tradeEngine.trade();
		}
		
		Evaluator eval = new Evaluator(compare, tradeEngine, orderBooks);
		List<Trade> strategyTrades = eval.filterStrategyTrades();
		if (strategyTrades.size() == 0) {
		} else {
			profit = eval.getProfit();
			Main.lblCompareResult.setText("$ " + String.format("%.2f", new Double(profit)));
			Main.lblCompareResult.update(Main.lblCompareResult.getGraphics());
		}
		double comparedProfit = 0;
		String interpret;
		
		if (profit > result) {
			comparedProfit = profit - result;			
			interpret = compare.getStrategyName() + " resulted in more profit by $ " + String.format("%.2f", new Double(comparedProfit));
		} else if (result > profit){
			comparedProfit = result - profit;
			interpret = strat.getStrategyName() + " resulted in more profit by $ " + String.format("%.2f", new Double(comparedProfit));
		} else {
			interpret = "Both " + compare.getStrategyName() + " and " + strat.getStrategyName() + " are equal in profit";
		}
		Main.lblDisplayResult.setText(interpret);
		Main.lblDisplayTotalBuyCom.setText(eval.getTotalBuy().toString());
		Main.lblDisplayTotalSellCom.setText(eval.getTotalSell().toString());
		Double returns = (((double)eval.getTotalSell() - (double)eval.getTotalBuy()) / (double)eval.getTotalBuy()) * 100;
		Main.lblDisplayReturnCom.setText(String.format("%.2f", returns) + " %");
		Chart.drawChartCompare(tradeEngine.getTradeList());
	}
	
	public static void updateProgressBar(Reader CSV) {
    	Main.progressPercent.setString(getProgress(CSV) + " %");
		Main.progressPercent.setValue(getProgressPercent(CSV));
		Main.progressPercent.update(Main.progressPercent.getGraphics());
	}
	
	public static void exitProgram() {
		System.exit(0);
	}

	public static Integer getProgressPercent (Reader CSV) {
		if (CSV != null) {
			float f = 100*((float)CSV.getProgress()/(float)CSV.getFileSize());
			Integer percentage = new Integer((int)f);
			return percentage;
		}
		return new Integer(0);
	}
	
	public static String getProgress(Reader CSV) {
		if (CSV != null) {
			Float percentage = new Float(100*((float)CSV.getProgress()/(float)CSV.getFileSize()));
			return String.format("%.2f ", percentage);
		}
		return new String(); 
	}
	
	public static void displayEvaluation(List<Trade> strategyTrades) {

		
		String tradeList = "<html><table> <tr><td width= 200>Bid ID</td>" +
				"<td width= 200>Ask ID</td>" +
				"<td width= 150>Price ($)</td>" +
				"<td width = 150>Volume</td></tr></table>"; 
		
		for (Trade t:strategyTrades) {

			tradeList += "<table> <tr><td width= 200>" + String.format("%019d", t.getBid().ID()) + "</td>" + 
					"<td width= 200>" + String.format("%019d", t.getAsk().ID()) + "</td>" +
					"<td width= 150>" + t.price().toString() + "</td>" + 
					"<td width = 100>" + new Integer(t.volume()).toString() + "</td></tr></table>";
		}
		
		
		tradeList += "</html>";
		Main.lblTradeList.setText(tradeList);
	}

	public static void showTrade(Trade t) {
		
		Main.lblTradeList.revalidate();
	}

}
