package simulator.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import simulator.Trade;

public class Chart  {

	private static XYSeries series;
	private static XYSeriesCollection dataset;
	private static ArrayList<Trade> tradeList;
	
	private static void initChart(ArrayList<Trade> list) {
		tradeList = list;
		series = new XYSeries("TradeGraph");
	}
	
	private static void addAll() {
		for(int i = 0; i < tradeList.size(); i++) {
			// first parameter can't be parseDouble because string includes ":"
			series.add(Double.parseDouble(tradeList.get(i).time()), tradeList.get(i).price());		
		}
	}

	private static void setCollection() {
		dataset = new XYSeriesCollection();
		dataset.addSeries(series);
	}
	
	public static void drawChart(ArrayList<Trade> list) {
		initChart(list);
		addAll();
		setCollection();
		JFreeChart chart = ChartFactory.createXYLineChart(
			"Trade Price Change", // Title
			"Time", // x-axis Label
			"Price", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			true, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
		);
		try {
			ChartUtilities.saveChartAsJPEG(new File("C:\\chart.jpg"), chart, 500, 300);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart.");
		}	
	}
}
