package simulator.gui;


import simulator.Factory;
import simulator.Reader;
import simulator.strategy.Strategy;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

	private JPanel contentPane;
	private static JTextField filePath;
	private static JPanel panelSimulation;
	JMenuBar menuBar;
	JMenu mnFile;
	JMenu mnEdit;
	JMenu mnHelp;
	JMenuItem mntmExit;
	JMenuItem mntmInstruction;
	JMenuItem mntmAboutAlgorithmicTrading;
	JLabel lblStrategy;
	JLabel lblDataFile;
	JLabel lblSelectedDateFile;
	JLabel lblSelectedStrategy;
	JLabel lblProgress;
	JLabel displayStrategy;
	JLabel lblProfit;
	JLabel lblTradesFromSelected;
	JLabel lblTotalBuying;
	JLabel lblTotalSelling;
	JLabel lblReturns;
	JLabel lblTotalBuyingToCompare;
	JLabel lblTotalSellingToCompare;
	JLabel lblReturnsToCompareT;
	static JLabel lblDisplayTotalBuy;
	static JLabel lblDisplayTotalSell;
	static JLabel lblDisplayRetuns;
	static JLabel lblDisplayTotalBuyCom;
	static JLabel lblDisplayTotalSellCom;
	static JLabel lblDisplayReturnCom;
	static JLabel displayData;
	static JLabel lbProfitResult;
	static JLabel lblTradeList;
	static JProgressBar progressPercent;
	JComboBox selectedStrategy;
	JComboBox selectedComparison;
	JButton runSimulation;
	JButton btnComparison;
	JButton resetSimulation;
	Factory factory;
	Reader CSV;
	Strategy selected;
	Strategy compared;
	JLabel lblStrategyToCompare;
	JLabel displayCompare;
	JLabel label;
	JLabel lblProfitToCompare;
	static JLabel lblCompareResult;
	double result;
	JLabel lblComparingResult;
	static JLabel lblDisplayResult;
	JPanel panel;	
    public static String userLookBackPeriod;
    public static String userSignalThreshold;
    public static String userPriceOffset;
    public static String userFrequency;
    public static String userRatio;
    public static String userVolumeLower;
    public static String userVolumeUpper;
    public static String userPriceLower;
    public static String userPriceUpper;

    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		initMain();
	}
	
	private void initMain() {

		setTitle("Algorithmic Trading System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 876, 730);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Start.exitProgram();
			}
		});
		mnFile.add(mntmExit);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmInstruction = new JMenuItem("Instruction");
		mntmInstruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JLabel lbl = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/Instruction.png"))).getImage()).getScaledInstance(650, 650, java.awt.Image.SCALE_SMOOTH)));
				//JLabel lbl = new JLabel(new ImageIcon(getClass().getResource("/Instruction.png")));
				//JLabel lbl = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/Instruction.png"));
			    JOptionPane.showMessageDialog(null, lbl, "Instruction",JOptionPane.PLAIN_MESSAGE, null);
			}
		});
		mnHelp.add(mntmInstruction);
		
		mntmAboutAlgorithmicTrading = new JMenuItem("About Algorithmic Trading System");
		mntmAboutAlgorithmicTrading.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(new JFrame(),
                        "<html>Algorithmic Trading System<br><br>Version: 1.0<br>Produced by: Yeri Chung, Jason Huang, Peter Godfrey, Sheryl Shi</html>",
                        "About Algorithmic Trading System",
                        JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnHelp.add(mntmAboutAlgorithmicTrading);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelSimulation = new JPanel();
		panelSimulation.setBounds(0, 0, 860, 671);
		contentPane.add(panelSimulation);
		panelSimulation.setLayout(null);
		
		lblSelectedDateFile = new JLabel("Selected Date File :");
		lblSelectedDateFile.setBounds(10, 196, 120, 20);
		panelSimulation.add(lblSelectedDateFile);
		
		lblSelectedStrategy = new JLabel("Selected Strategy :");
		lblSelectedStrategy.setBounds(10, 227, 120, 20);
		panelSimulation.add(lblSelectedStrategy);
		
		lblProgress = new JLabel("Progress :");
		lblProgress.setBounds(10, 165, 74, 20);
		panelSimulation.add(lblProgress);
		
		displayData = new JLabel("");
		displayData.setBounds(124, 196, 691, 20);
		panelSimulation.add(displayData);
		
		displayStrategy = new JLabel("");
		displayStrategy.setBounds(123, 227, 257, 20);
		panelSimulation.add(displayStrategy);
		
		lblDataFile = new JLabel("Data File Path :");
		lblDataFile.setBounds(10, 11, 120, 20);
		panelSimulation.add(lblDataFile);
		
		
		filePath = new JTextField();
		filePath.setBounds(124, 11, 713, 20);
		panelSimulation.add(filePath);
		filePath.setText("Enter the filepath of the selected CSV file");
		filePath.setToolTipText("For example, \"C:\\User\\user\\Desktop\\sircaData.csv");
		filePath.setColumns(10);
		
		lblStrategy = new JLabel("Strategy :");
		lblStrategy.setBounds(10, 38, 61, 20);
		panelSimulation.add(lblStrategy);
		
		final String[] strategyOptions = {"Select one of strategies", "Mean Reversion", "Momentum", "Dumb", "Random", "Null"};
		selectedStrategy = new JComboBox (strategyOptions);
		selectedStrategy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((String) selectedStrategy.getSelectedItem() == strategyOptions[1]) {
			        JTextField textLookBackPeriod = new JTextField("10");
			        JTextField textSignalThreshold = new JTextField("0");
			        JTextField textPriceOffset = new JTextField("0");
			        JPanel panel = new JPanel(new GridLayout(0, 1));
			        panel.add(new JLabel("Look Back Period :"));
			        panel.add(textLookBackPeriod);
			        panel.add(new JLabel("Signal Threshold :"));
			        panel.add(textSignalThreshold);
			        panel.add(new JLabel("Price Offset : "));
			        panel.add(textPriceOffset);
			        panel.add(new JLabel("<html> <br>" + "If you don't type anything, default values will be used" + "<br> </html>"));
			        JOptionPane.showConfirmDialog(null, panel, "MeanReversion Parameter", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
			        userLookBackPeriod = textLookBackPeriod.getText();
			        userSignalThreshold = textSignalThreshold.getText();
			        userPriceOffset = textPriceOffset.getText();
				}				
				if ((String) selectedStrategy.getSelectedItem() == strategyOptions[2]) {
			        JTextField textLookBackPeriod = new JTextField("10");
			        JTextField textSignalThreshold = new JTextField("0");
			        JTextField textPriceOffset = new JTextField("0");
			        JPanel panel = new JPanel(new GridLayout(0, 1));
			        panel.add(new JLabel("Look Back Period :"));
			        panel.add(textLookBackPeriod);
			        panel.add(new JLabel("Signal Threshold :"));
			        panel.add(textSignalThreshold);
			        panel.add(new JLabel("Price Offset : "));
			        panel.add(textPriceOffset);
			        panel.add(new JLabel("<html> <br>" + "If you don't type anything, default values will be used" + "<br> </html>"));
			        JOptionPane.showConfirmDialog(null, panel, "Momentum Parameter", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
			        userLookBackPeriod = textLookBackPeriod.getText();
			        userSignalThreshold = textSignalThreshold.getText();
			        userPriceOffset = textPriceOffset.getText();
				}
				if ((String) selectedStrategy.getSelectedItem() == strategyOptions[4]) {
			        JTextField textFrequency = new JTextField("0.5");
			        JTextField textRatio = new JTextField("0.1");
			        JTextField textVolumeLower = new JTextField("0");
			        JTextField textVolumeUpper = new JTextField("100");
			        JTextField textPriceLower = new JTextField("0");
			        JTextField textPriceUpper = new JTextField("100");			        
			        JPanel panel = new JPanel(new GridLayout(0, 1));
			        panel.add(new JLabel("Frequency of order generation :"));
			        panel.add(textFrequency);
			        panel.add(new JLabel("Ratio of bid and ask orders :"));
			        panel.add(textRatio);
			        panel.add(new JLabel("Volume lower bound : "));
			        panel.add(textVolumeLower);
			        panel.add(new JLabel("Volume upper bound :"));
			        panel.add(textVolumeUpper);
			        panel.add(new JLabel("Price lower bound : $ "));
			        panel.add(textPriceLower);
			        panel.add(new JLabel("Price upper bound : $ "));
			        panel.add(textPriceUpper);			        
			        panel.add(new JLabel("<html> <br>" + "If you don't type anything, default values will be used" + "<br> </html>"));
			        JOptionPane.showConfirmDialog(null, panel, "Random Parameter", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			        userFrequency = textFrequency.getText();
			        userRatio = textRatio.getText();
			        userVolumeLower = textVolumeLower.getText();
			        userVolumeUpper = textVolumeUpper.getText();
			        userPriceLower = textPriceLower.getText();
			        userPriceUpper = textPriceUpper.getText();
				}
			}
		});
		selectedStrategy.setBounds(124, 38, 250, 20);
		panelSimulation.add(selectedStrategy);
		selectedStrategy.setSelectedIndex(0);
		selectedStrategy.setToolTipText("Click the arrow and select one of strategies");
		selectedStrategy.setMaximumRowCount(6);
		
		progressPercent = new JProgressBar();
		progressPercent.setForeground(SystemColor.textHighlight);
		progressPercent.setBounds(124, 165, 713, 20);
		panelSimulation.add(progressPercent);
		progressPercent.setStringPainted(true);
				
		runSimulation = new JButton("Run Simulation");
		runSimulation.setBounds(406, 92, 130, 23);
		panelSimulation.add(runSimulation);
		
		resetSimulation = new JButton("Reset Simulation");
		resetSimulation.setBounds(685, 92, 130, 23);
		panelSimulation.add(resetSimulation);
		
		lblProfit = new JLabel("Profit :");
		//lblProfit.setBounds(10, 258, 120, 20);
		lblProfit.setBounds(10, 333, 61, 20);
		panelSimulation.add(lblProfit);
		
		lbProfitResult = new JLabel("$");
		lbProfitResult.setBounds(124, 333, 222, 20);
		panelSimulation.add(lbProfitResult);
		
		lblTradesFromSelected = new JLabel("Trade list :");
		//lblTradesFromSelected.setBounds(10, 289, 120, 20);
		lblTradesFromSelected.setBounds(10, 364, 120, 20);
		panelSimulation.add(lblTradesFromSelected);
		
		lblTradeList = new JLabel("<html><table> <tr><td width= 200>Bid ID</td>" +
				"<td width= 200>Ask ID</td>" +
				"<td width= 150>Price ($)</td>" +
				"<td width = 100>Volume</td></tr></table></html>");
		
		JScrollPane scrollTradeList = new JScrollPane(lblTradeList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTradeList.setBounds(124, 366, 695, 251);		
		panelSimulation.add(scrollTradeList);
		lblTradeList.setVerticalAlignment(SwingConstants.TOP);

		lblStrategyToCompare = new JLabel("Strategy to compare :");
		lblStrategyToCompare.setBounds(390, 227, 140, 20);
		panelSimulation.add(lblStrategyToCompare);
		
		displayCompare = new JLabel("");
		displayCompare.setBounds(558, 227, 257, 20);
		panelSimulation.add(displayCompare);
		
		label = new JLabel("Strategy :");
		label.setBounds(394, 38, 61, 20);
		panelSimulation.add(label);
		
		selectedComparison = new JComboBox(strategyOptions);
		selectedComparison.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((String) selectedComparison.getSelectedItem() == strategyOptions[1]) {
			        JTextField textLookBackPeriod = new JTextField("10");
			        JTextField textSignalThreshold = new JTextField("0");
			        JTextField textPriceOffset = new JTextField("0");
			        JPanel panel = new JPanel(new GridLayout(0, 1));
			        panel.add(new JLabel("Look Back Period :"));
			        panel.add(textLookBackPeriod);
			        panel.add(new JLabel("Signal Threshold :"));
			        panel.add(textSignalThreshold);
			        panel.add(new JLabel("Price Offset : "));
			        panel.add(textPriceOffset);
			        panel.add(new JLabel("<html> <br>" + "If you don't type anything, default values will be used" + "<br> </html>"));
			        JOptionPane.showConfirmDialog(null, panel, "MeanReversion Parameter", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
			        userLookBackPeriod = textLookBackPeriod.getText();
			        userSignalThreshold = textSignalThreshold.getText();
			        userPriceOffset = textPriceOffset.getText();
				}	
				if ((String) selectedComparison.getSelectedItem() == strategyOptions[2]) {
			        JTextField textLookBackPeriod = new JTextField("10");
			        JTextField textSignalThreshold = new JTextField("0");
			        JTextField textPriceOffset = new JTextField("0");
			        JPanel panel = new JPanel(new GridLayout(0, 1));
			        panel.add(new JLabel("Look Back Period :"));
			        panel.add(textLookBackPeriod);
			        panel.add(new JLabel("Signal Threshold :"));
			        panel.add(textSignalThreshold);
			        panel.add(new JLabel("Price Offset : "));
			        panel.add(textPriceOffset);
			        panel.add(new JLabel("<html> <br>" + "If you don't type anything, default values will be used" + "<br> </html>"));
			        JOptionPane.showConfirmDialog(null, panel, "Momentum Parameter", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
			        userLookBackPeriod = textLookBackPeriod.getText();
			        userSignalThreshold = textSignalThreshold.getText();
			        userPriceOffset = textPriceOffset.getText();
				}
				if ((String) selectedComparison.getSelectedItem() == strategyOptions[4]) {
			        JTextField textFrequency = new JTextField("0.5");
			        JTextField textRatio = new JTextField("0.1");
			        JTextField textVolumeLower = new JTextField("0");
			        JTextField textVolumeUpper = new JTextField("100");
			        JTextField textPriceLower = new JTextField("0");
			        JTextField textPriceUpper = new JTextField("100");			        
			        JPanel panel = new JPanel(new GridLayout(0, 1));
			        panel.add(new JLabel("Frequency of order generation :"));
			        panel.add(textFrequency);
			        panel.add(new JLabel("Ratio of bid and ask orders :"));
			        panel.add(textRatio);
			        panel.add(new JLabel("Volume lower bound : "));
			        panel.add(textVolumeLower);
			        panel.add(new JLabel("Volume upper bound :"));
			        panel.add(textVolumeUpper);
			        panel.add(new JLabel("Price lower bound : $"));
			        panel.add(textPriceLower);
			        panel.add(new JLabel("Price upper bound : $"));
			        panel.add(textPriceUpper);			        
			        panel.add(new JLabel("<html> <br>" + "If you don't type anything, default values will be used" + "<br> </html>"));
			        JOptionPane.showConfirmDialog(null, panel, "Random Parameter", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			        userFrequency = textFrequency.getText();
			        userRatio = textRatio.getText();
			        userVolumeLower = textVolumeLower.getText();
			        userVolumeUpper = textVolumeUpper.getText();
			        userPriceLower = textPriceLower.getText();
			        userPriceUpper = textPriceUpper.getText();
				}
			}
		});
		selectedComparison.setToolTipText("Click the arrow and select one of strategies");
		selectedComparison.setSelectedIndex(0);
		selectedComparison.setMaximumRowCount(6);
		selectedComparison.setBounds(524, 38, 250, 20);
		panelSimulation.add(selectedComparison);
		
		btnComparison = new JButton("Run Comparison");
		btnComparison.addMouseListener(new MouseAdapter() {
	
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String strat = (String) selectedComparison.getSelectedItem();
				if ((String) selectedStrategy.getSelectedItem() == "Select one of strategies") {
					JOptionPane.showMessageDialog(new JFrame(),
                            "There's no result to compare, please run simulation first.",
                            "Comparison error",
                            JOptionPane.ERROR_MESSAGE);
					displayStrategy.setText("");
				} else if (strat == (String) selectedStrategy.getSelectedItem()) {
	                   JOptionPane.showMessageDialog(new JFrame(),
                               "Please choose different strategy for comparison.",
                               "Comparison strategy error",
                               JOptionPane.ERROR_MESSAGE);
					displayStrategy.setText("");
	   				displayCompare.setText("");
				} else {
					if (strat == "Mean Reversion" || strat == "Momentum" || strat == "Dumb" || strat == "Random" || strat == "Null") {
						compared = Start.selectComparison(strat, factory);
		   				displayCompare.setText("");
						displayCompare.setText(strat);
						displayCompare.update(displayCompare.getGraphics());
						Start.runComparison(CSV, compared, selected, factory, result);
					} else {
		                   JOptionPane.showMessageDialog(new JFrame(),
                                   "Strategy is not selected.",
                                   "Comparison error",
                                   JOptionPane.ERROR_MESSAGE);
						displayStrategy.setText("");   
		   				displayCompare.setText("");
					}
				}	
			}
		});
		btnComparison.setBounds(546, 92, 130, 23);
		panelSimulation.add(btnComparison);
		
		lblProfitToCompare = new JLabel("Profit to compare :");
		//lblProfitToCompare.setBounds(390, 258, 130, 20);
		lblProfitToCompare.setBounds(390, 333, 130, 20);
		panelSimulation.add(lblProfitToCompare);
		
		lblCompareResult = new JLabel("$");
		lblCompareResult.setToolTipText("");
		lblCompareResult.setBounds(520, 334, 222, 18);
		panelSimulation.add(lblCompareResult);
		
		lblComparingResult = new JLabel("Comparing Result :");
		//lblComparingResult.setBounds(10, 576, 120, 20);
		lblComparingResult.setBounds(10, 637, 120, 20);
		panelSimulation.add(lblComparingResult);
		
		lblDisplayResult = new JLabel("");
		lblDisplayResult.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		lblDisplayResult.setBounds(124, 637, 691, 20);
		panelSimulation.add(lblDisplayResult);
		
		lblTotalBuying = new JLabel("Total Buying :");
		lblTotalBuying.setBounds(10, 258, 103, 14);
		panelSimulation.add(lblTotalBuying);
		
		lblTotalSelling = new JLabel("Total Selling :");
		lblTotalSelling.setBounds(10, 283, 103, 14);
		panelSimulation.add(lblTotalSelling);
		
		lblReturns = new JLabel("Returns :");
		lblReturns.setBounds(10, 308, 60, 14);
		panelSimulation.add(lblReturns);
		
		lblTotalBuyingToCompare = new JLabel("Total Buying to compare:");
		lblTotalBuyingToCompare.setBounds(390, 258, 170, 14);
		panelSimulation.add(lblTotalBuyingToCompare);
		
		lblTotalSellingToCompare = new JLabel("Total Selling to compare :");
		lblTotalSellingToCompare.setBounds(390, 283, 170, 14);
		panelSimulation.add(lblTotalSellingToCompare);
		
		lblReturnsToCompareT = new JLabel("Returns to compare :");
		lblReturnsToCompareT.setBounds(390, 308, 150, 14);
		panelSimulation.add(lblReturnsToCompareT);
		
		lblDisplayTotalBuy = new JLabel("");
		lblDisplayTotalBuy.setBounds(124, 258, 222, 14);
		panelSimulation.add(lblDisplayTotalBuy);
		
		lblDisplayTotalSell = new JLabel("");
		lblDisplayTotalSell.setBounds(124, 283, 222, 14);
		panelSimulation.add(lblDisplayTotalSell);
		
		lblDisplayRetuns = new JLabel("");
		lblDisplayRetuns.setBounds(124, 308, 222, 14);
		panelSimulation.add(lblDisplayRetuns);
		
		lblDisplayTotalBuyCom = new JLabel("");
		lblDisplayTotalBuyCom.setBounds(558, 258, 257, 14);
		panelSimulation.add(lblDisplayTotalBuyCom);
		
		lblDisplayTotalSellCom = new JLabel("");
		lblDisplayTotalSellCom.setBounds(558, 283, 257, 14);
		panelSimulation.add(lblDisplayTotalSellCom);
		
		lblDisplayReturnCom = new JLabel("");
		lblDisplayReturnCom.setBounds(558, 309, 257, 14);
		panelSimulation.add(lblDisplayReturnCom);
				
		resetSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filePath.setText("Enter the filepath of the selected CSV file");
				selectedStrategy.setSelectedIndex(0);
				displayData.setText("");
				displayStrategy.setText("");
				displayCompare.setText("");
				lblDisplayResult.setText("");
				progressPercent.setString("0 %");
				progressPercent.setValue(0);
				Main.lblTradeList.setText("");
				Main.lbProfitResult.setText("$ ");
				Main.lblCompareResult.setText("$ ");
				Main.lblDisplayTotalBuy.setText("");
				Main.lblDisplayTotalSell.setText("");
				Main.lblDisplayRetuns.setText("");
				Main.lblDisplayTotalBuyCom.setText("");
				Main.lblDisplayTotalSellCom.setText("");
				Main.lblDisplayReturnCom.setText("");
			}
		});
				
		runSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				clear();
				factory = new Factory();
				CSV = Start.getFilePath(filePath.getText(), factory);
				if (CSV != null) {
					String strat = (String) selectedStrategy.getSelectedItem();
					if (strat == "Mean Reversion" || strat == "Momentum" || strat == "Dumb" || strat == "Random" || strat == "Null") {
						selected = Start.selectStrategy(strat, factory);
					} else {
						JOptionPane.showMessageDialog(new JFrame(),
	                            "Strategy is not selected",
	                            "Strategy error",
	                            JOptionPane.ERROR_MESSAGE);
					}
					if (strat != "Select one of strategies") {
						displayData.setText(filePath.getText());
						displayData.update(displayData.getGraphics());
						displayStrategy.setText(strat);
						displayStrategy.update(displayStrategy.getGraphics());
						result = Start.runSimulation(CSV, selected, factory);
					}
				}
			}
		});
		
	}
	
	private void clear () {
		displayData.setText("");
		displayData.update(displayData.getGraphics());
		displayStrategy.setText("");
		displayStrategy.update(displayStrategy.getGraphics());
		displayCompare.setText("");
		lblDisplayResult.setText("");
		progressPercent.setString("0 %");
		progressPercent.setValue(0);
		Main.lblTradeList.setText("");
		Main.lbProfitResult.setText("$ ");
		Main.lblCompareResult.setText("$ ");
		Main.lblDisplayTotalBuy.setText("");
		Main.lblDisplayTotalSell.setText("");
		Main.lblDisplayRetuns.setText("");
		Main.lblDisplayTotalBuyCom.setText("");
		Main.lblDisplayTotalSellCom.setText("");
		Main.lblDisplayReturnCom.setText("");
		Chart.deleteChart();
	}
}
