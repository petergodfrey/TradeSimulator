package simulator.gui;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

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
			System.out.println(pathInput);
			CSV = factory.makeReader(pathInput);
		} catch (Exception e) {
			// change to appear popup window to alert invalid input.
			System.out.println("Invalid filepath, enter the correct path.");
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
			strat = factory.makeNewStrategy();
		}
		return strat;
	}

	public static void runSimulation(Reader CSV, Strategy strat, Factory factory) {
		long initTime = System.currentTimeMillis();
		//cannot run simulation if there is no CSV chosen
		if (CSV == null) {
			System.out.println("A CSV file has not been selected, cannot run simulation"); 
			return;//exit function early
		}

		SignalGenerator signalGenerator = null;
		try {
			CSV = factory.makeReader(CSV.getFilePath());
			System.out.println("Loading " + CSV.getFilePath());
			signalGenerator = new SignalGenerator(CSV, strat, factory);
		} catch (IOException e) {
			System.out.println("Error in reading CSV file, exiting simulation");
			return;
		}

		OrderBooks  orderBooks  = factory.makeOrderBooks();
		TradeEngine tradeEngine = factory.makeTradeEngine();
		//ensure each simulation run begins with empty orderbooks and trade lists
		//ensures successive simulations are unaffected
		orderBooks.resetOrderBooks();
		tradeEngine.resetTradeEngine();
		
		//reset any strategy attributes after each simulation
		strat.reset();

		System.out.println("Running simulation ");

		Order o;
		
		while ((o = signalGenerator.advance()) != null) {
			//one iteration equals one order being processed and traded
			orderBooks.processOrder(o);
			tradeEngine.trade();
			displayProgress(CSV, orderBooks);

			updateProgressBar(CSV);
		}
		
		Evaluator eval = new Evaluator(strat, tradeEngine, orderBooks);
		System.out.println("\nFinished Simulation, now evaluating...\n");
		List<Trade> strategyTrades = eval.evaluate();
		if (strategyTrades.size() == 0) {
			System.out.println("STRATEGY CREATED NO TRADES");
			//return;
		} else {
			displayEvaluation(strategyTrades);
			System.out.println("\nSumming up the buys and sells\n");
			Integer profit = new Integer (eval.calculateProfit(strategyTrades));
			System.out.println("Profit: $("+profit+")!");
			Main.lbProfitResult.setText("$ " + profit.toString());
		}
		//Chart.drawChart(tradeEngine.getTradeList());
		long finalTime = System.currentTimeMillis();
		double millisTaken = (double)finalTime-(double)initTime;
		System.out.printf("\nTime Taken: %.2f seconds\n",millisTaken/1000);
		//System.out.println("Time Taken: "+(System.currentTimeMillis()-initTime)/1000+" seconds");
		
		System.out.println("\n###########################################");
	}
	
	public static void updateProgressBar(Reader CSV) {
    	Main.progressPercent.setString(getProgress(CSV) + " %");
		Main.progressPercent.setValue(getProgressPercent(CSV));
		Main.progressPercent.update(Main.progressPercent.getGraphics());
	}
	
	public static void exitProgram() {
		System.out.println("Cya!");
		System.exit(0);
	}

	public static void displayProgress(Reader CSV, OrderBooks books) {
		System.out.printf("\rsimulated time: %s | %.2f percent done",
				books.getSimulatedTime(),
				100*((float)CSV.getProgress()/(float)CSV.getFileSize()));
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

		System.out.println("Printing the trades generated by Strategy\n");
		//displayBidAskHeader();
		//for (Trade t:strategyTrades) {
		//	displayBidAskofTrade(t);
		//}
		displayTradeHeader();
		
		String bidLine = "<html>Bid ID<br>";
		String askLine = "<html>Ask ID<br>";
		String priceLine = "<html>price<br>";
		String volumeLine = "<html>Volume<br>";

		for (Trade t:strategyTrades) {
			displayTrade(t);
			bidLine += String.format("%019d", t.getBid().ID())  + "<br>";
			askLine += String.format("%019d", t.getAsk().ID())  + "<br>";
			priceLine += t.price().toString()  + "<br>";
			volumeLine += new Integer(t.volume()).toString() + "<br>";
		}
		bidLine += "</html>";
		askLine += "</html>";
		priceLine += "</html>";
		volumeLine += "</html>";
		Main.lblBidID.setText(bidLine);
		Main.lblAskID.setText(askLine);
		Main.lblPrice.setText(priceLine);
		Main.lblVolume.setText(volumeLine);
	}

	public static void showTrade(Trade t) {
		
		Main.lblBidID.revalidate();
	}
	
	public static void displayTradeHeader() {
		System.out.print("Bid ID\t\t\t");
		System.out.print("Ask ID\t\t\t");
		System.out.print("Price\t");
		System.out.print("Volume");
		System.out.println();
	}

	public static void displayBidAskHeader() {
		System.out.print("Bid ID\t\t\t");
		System.out.print("Price\t");
		System.out.print("Volume\t|");

		System.out.print("Ask ID\t\t\t");
		System.out.print("Price\t");
		System.out.print("Volume");
		System.out.println();
	}

	public static void displayBidAskofTrade(Trade t) {
		System.out.print(String.format("%019d", t.getBid().ID())+"\t");
		System.out.print(t.getBid().price()+"\t");
		System.out.print(t.getBid().volume() + "\t|");

		System.out.print(String.format("%019d", t.getAsk().ID())+"\t");
		System.out.print(t.getAsk().price()+"\t");
		System.out.print(t.getAsk().volume());
		System.out.println();
	}

	public static void displayTrade(Trade t) {

		System.out.print(String.format("%019d", t.getBid().ID())+"\t");
		System.out.print(String.format("%019d", t.getAsk().ID())+"\t");
		System.out.print(t.price()+"\t");
		System.out.print(t.volume());

		System.out.println();
	}

}