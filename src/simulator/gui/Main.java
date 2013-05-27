package simulator.gui;


import simulator.Factory;
import simulator.Reader;
import simulator.strategy.Strategy;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import javax.swing.JScrollBar;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

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
	static JLabel displayData;
	static JLabel lbProfitResult;
	static JLabel lblBidID;
	static JLabel lblAskID;
	static JLabel lblPrice;
	static JLabel lblVolume;
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
	int result;
	JLabel lblComparingResult;
	static JLabel lblDisplayResult;
		
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
		setBounds(100, 100, 842, 688);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Start.exitProgram();
			}
		});
		mnFile.add(mntmExit);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmInstruction = new JMenuItem("Instruction");
		mnHelp.add(mntmInstruction);
		
		mntmAboutAlgorithmicTrading = new JMenuItem("About Algorithmic Trading System");
		mnHelp.add(mntmAboutAlgorithmicTrading);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelSimulation = new JPanel();
		panelSimulation.setBounds(0, 0, 825, 629);
		contentPane.add(panelSimulation);
		panelSimulation.setLayout(null);
		
		lblSelectedDateFile = new JLabel("Selected Date File :");
		lblSelectedDateFile.setBounds(10, 196, 120, 20);
		panelSimulation.add(lblSelectedDateFile);
		
		lblSelectedStrategy = new JLabel("Selected Strategy :");
		lblSelectedStrategy.setBounds(10, 227, 103, 20);
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
		lblDataFile.setBounds(10, 11, 74, 20);
		panelSimulation.add(lblDataFile);
		
		
		filePath = new JTextField();
		filePath.setBounds(124, 11, 683, 20);
		panelSimulation.add(filePath);
		filePath.setText("Enter the filepath of the selected CSV file");
		filePath.setToolTipText("For example, \"C:\\User\\user\\Desktop\\sircaData.csv");
		filePath.setColumns(10);
		
		lblStrategy = new JLabel("Strategy :");
		lblStrategy.setBounds(10, 38, 61, 20);
		panelSimulation.add(lblStrategy);
		
		String[] strategyOptions = {"Select one of strategies", "Mean Reversion", "Momentum", "Dumb", "Random"};
		selectedStrategy = new JComboBox (strategyOptions);
		selectedStrategy.setBounds(124, 38, 176, 20);
		panelSimulation.add(selectedStrategy);
		selectedStrategy.setSelectedIndex(0);
		selectedStrategy.setToolTipText("Click the arrow and select one of strategies");
		selectedStrategy.setMaximumRowCount(5);
		
		progressPercent = new JProgressBar();
		progressPercent.setForeground(SystemColor.textHighlight);
		progressPercent.setBounds(124, 165, 691, 20);
		panelSimulation.add(progressPercent);
		progressPercent.setStringPainted(true);
				
		runSimulation = new JButton("Run Simulation");
		runSimulation.setBounds(436, 92, 120, 23);
		panelSimulation.add(runSimulation);
		
		resetSimulation = new JButton("Reset Simulation");
		resetSimulation.setBounds(695, 92, 120, 23);
		panelSimulation.add(resetSimulation);
		
		lblProfit = new JLabel("Profit :");
		lblProfit.setBounds(10, 258, 61, 20);
		panelSimulation.add(lblProfit);
		
		lbProfitResult = new JLabel("$");
		lbProfitResult.setBounds(124, 258, 222, 20);
		panelSimulation.add(lbProfitResult);
		
		lblTradesFromSelected = new JLabel("Trade list :");
		lblTradesFromSelected.setBounds(10, 289, 61, 20);
		panelSimulation.add(lblTradesFromSelected);
		
		lblBidID = new JLabel("Bid ID");
		lblBidID.setVerticalAlignment(SwingConstants.TOP);
		lblBidID.setBounds(124, 292, 222, 251);
		panelSimulation.add(lblBidID);
		
		lblAskID = new JLabel("Ask ID");
		lblAskID.setVerticalAlignment(SwingConstants.TOP);
		lblAskID.setBounds(356, 292, 222, 251);
		panelSimulation.add(lblAskID);
		
		lblPrice = new JLabel("Price");
		lblPrice.setVerticalAlignment(SwingConstants.TOP);
		lblPrice.setBounds(604, 292, 74, 251);
		panelSimulation.add(lblPrice);
		
		lblVolume = new JLabel("Volume");
		lblVolume.setVerticalAlignment(SwingConstants.TOP);
		lblVolume.setBounds(696, 292, 74, 251);
		panelSimulation.add(lblVolume);
		
		lblStrategyToCompare = new JLabel("Strategy to compare :");
		lblStrategyToCompare.setBounds(390, 227, 120, 20);
		panelSimulation.add(lblStrategyToCompare);
		
		displayCompare = new JLabel("");
		displayCompare.setBounds(520, 227, 257, 20);
		panelSimulation.add(displayCompare);
		
		label = new JLabel("Strategy :");
		label.setBounds(394, 38, 61, 20);
		panelSimulation.add(label);
		
		selectedComparison = new JComboBox(strategyOptions);
		selectedComparison.setToolTipText("Click the arrow and select one of strategies");
		selectedComparison.setSelectedIndex(0);
		selectedComparison.setMaximumRowCount(5);
		selectedComparison.setBounds(524, 38, 176, 20);
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
					if (strat == "Mean Reversion" || strat == "Momentum" || strat == "Dumb" || strat == "Random") {
						compared = Start.selectComparison(strat, factory);
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
		btnComparison.setBounds(566, 92, 120, 23);
		panelSimulation.add(btnComparison);
		
		lblProfitToCompare = new JLabel("Profit to compare :");
		lblProfitToCompare.setBounds(390, 258, 103, 20);
		panelSimulation.add(lblProfitToCompare);
		
		lblCompareResult = new JLabel("$");
		lblCompareResult.setToolTipText("");
		lblCompareResult.setBounds(520, 259, 222, 18);
		panelSimulation.add(lblCompareResult);
		
		lblComparingResult = new JLabel("Comparing Result :");
		lblComparingResult.setBounds(10, 576, 103, 20);
		panelSimulation.add(lblComparingResult);
		
		lblDisplayResult = new JLabel("");
		lblDisplayResult.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		lblDisplayResult.setBounds(124, 576, 691, 20);
		panelSimulation.add(lblDisplayResult);
		
		resetSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filePath.setText("Enter the filepath of the selected CSV file");
				selectedStrategy.setSelectedIndex(0);
				displayData.setText("");
				//displayData.update(displayData.getGraphics());
				displayStrategy.setText("");
				//displayStrategy.update(displayStrategy.getGraphics());
				displayCompare.setText("");
				//displayCompare.update(displayCompare.getGraphics());
				lblDisplayResult.setText("");
				progressPercent.setString("0 %");
				progressPercent.setValue(0);
				Main.lblBidID.setText("");
				Main.lblAskID.setText("");
				Main.lblPrice.setText("");
				Main.lblVolume.setText("");
				Main.lbProfitResult.setText("$ ");
				Main.lblCompareResult.setText("$ ");
			}
		});
		
		runSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				factory = new Factory();
				CSV = Start.getFilePath(filePath.getText(), factory);
				if (CSV != null) {
					String strat = (String) selectedStrategy.getSelectedItem();
					if (strat == "Mean Reversion" || strat == "Momentum" || strat == "Dumb" || strat == "Random") {
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
}
