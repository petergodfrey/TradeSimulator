package simulator.gui;


import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import simulator.OrderBooks;
import simulator.Trade;

public class Chart  {

	private static XYSeries series;
	private static XYSeries series2;
	private static XYSeriesCollection dataset;
	private static ArrayList<Trade> tradeList;
	private static ArrayList<Trade> compareList;
	private static int chartIndex = 0;
	
	private static void initChart(ArrayList<Trade> list) {
		tradeList = list;
		series = new XYSeries("Source");
	}

	private static void initChartCompare(ArrayList<Trade> list) {
		compareList = list;
		series2 = new XYSeries("Compare"); 
	}
	
	private static void addAll() {
		for(int i = 0; i < tradeList.size(); i++) {
			if (OrderBooks.convertTimeToMilliseconds(tradeList.get(i).time()) >= 36000000) {
				if (tradeList.get(i).getBid().ID().compareTo(BigInteger.valueOf(0)) == -1 || tradeList.get(i).getAsk().ID().compareTo(BigInteger.valueOf(0)) == -1) {
					series.add(OrderBooks.convertTimeToMilliseconds(tradeList.get(i).time()), tradeList.get(i).price());		
				}
			}
		}
	}

	private static void addAllCompare() {
		for(int i = 0; i < compareList.size(); i++) {
			if (OrderBooks.convertTimeToMilliseconds(compareList.get(i).time()) >= 36000000) {			
				if (compareList.get(i).getBid().ID().compareTo(BigInteger.valueOf(0)) == -1 || compareList.get(i).getAsk().ID().compareTo(BigInteger.valueOf(0)) == -1) {
					series2.add(OrderBooks.convertTimeToMilliseconds(compareList.get(i).time()), compareList.get(i).price());
				}
			}
		}			
	}
	
	private static void setCollection() {
		dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		if (series2 != null) {
			dataset.addSeries(series2);
		}
	}
	
	public static void deleteChart() {
		File file = new File(System.getProperty("user.dir") + "/chart" + Integer.toString(chartIndex-1) + ".jpg");
		if (file.exists()) {
			file.delete();
		}	
	}
	
	public static void drawChart(ArrayList<Trade> source) {
		initChart(source);
		addAll();
		setCollection();
		JFreeChart chart = ChartFactory.createXYLineChart(
			"Trade Price Change", // Title
			"Time (Ms)", // x-axis Label
			"Price($)", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			true, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
		);
		try {
		    deleteChart();
			ChartUtilities.saveChartAsJPEG(new File(System.getProperty("user.dir") + "/chart" + Integer.toString(chartIndex) + ".jpg"), chart, 500, 300);
		    JLabel lbl = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/chart" + Integer.toString(chartIndex++) + ".jpg"));
		    
		    JOptionPane.showMessageDialog(null, lbl, "Trade Pattern", JOptionPane.PLAIN_MESSAGE, null);
		} catch (IOException e) {
			System.err.println(e);
			System.err.println("Problem occurred creating chart.");
		}	
	}
	
	public static void drawChartCompare (ArrayList<Trade> list) {
		initChartCompare(list);
		addAllCompare();
		setCollection();
		JFreeChart chart = ChartFactory.createXYLineChart(
			"Trade Price Change", // Title
			"Time (Ms)", // x-axis Label
			"Price($)", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			true, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
		);
		try {
			deleteChart();
			ChartUtilities.saveChartAsJPEG(new File(System.getProperty("user.dir") + "/chart" + Integer.toString(chartIndex) + ".jpg"), chart, 500, 300);
		    JLabel lbl = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/chart" + Integer.toString(chartIndex++) + ".jpg"));
		    JOptionPane.showMessageDialog(null, lbl, "Trade Pattern",JOptionPane.PLAIN_MESSAGE, null);
		} catch (IOException e) {
			System.err.println(e);
			System.err.println("Problem occurred creating chart.");
		}	
	}
}
