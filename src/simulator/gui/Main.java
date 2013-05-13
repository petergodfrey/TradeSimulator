package simulator.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import simulator.Factory;
import simulator.Reader;
import simulator.strategy.Strategy;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.SystemColor;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import javax.swing.SwingConstants;

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
	static JLabel displayData;
	JLabel displayStrategy;
	JLabel lblProfit;
	static JLabel lbProfitResult;
	JLabel lblTrades;
	static JLabel lblBidID;
	static JLabel lblAskID;
	static JLabel lblPrice;
	static JLabel lblVolume;
	JComboBox<String> selectedStrategy;
	JButton runSimulation;
	JButton cancelSimulation;
	static JProgressBar progressPercent;
	Factory factory;
	Reader CSV;
	Strategy selected;

	
	
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
		setBounds(100, 100, 841, 617);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmExit = new JMenuItem("Exit");
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
		panelSimulation.setBounds(0, 0, 825, 556);
		contentPane.add(panelSimulation);
		panelSimulation.setLayout(null);
		
		lblSelectedDateFile = new JLabel("Selected Date File :");
		lblSelectedDateFile.setBounds(10, 119, 120, 20);
		panelSimulation.add(lblSelectedDateFile);
		
		lblSelectedStrategy = new JLabel("Selected Strategy :");
		lblSelectedStrategy.setBounds(10, 151, 1120, 20);
		panelSimulation.add(lblSelectedStrategy);
		
		lblProgress = new JLabel("Progress :");
		lblProgress.setBounds(10, 182, 120, 20);
		panelSimulation.add(lblProgress);
		
		displayData = new JLabel("");
		displayData.setBounds(124, 119, 691, 20);
		panelSimulation.add(displayData);
		
		displayStrategy = new JLabel("");
		displayStrategy.setBounds(124, 151, 691, 20);
		panelSimulation.add(displayStrategy);
		
		lblDataFile = new JLabel("Data File Path :");
		lblDataFile.setBounds(10, 11, 74, 20);
		panelSimulation.add(lblDataFile);
		
		
		filePath = new JTextField();
		filePath.setBounds(124, 11, 683, 20);
		panelSimulation.add(filePath);
		filePath.setText("Type the filepath of the selected CSV file");
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
		progressPercent.setBounds(124, 184, 691, 20);
		panelSimulation.add(progressPercent);
		progressPercent.setStringPainted(true);
				
		runSimulation = new JButton("Run Simulation");
		runSimulation.setBounds(590, 92, 103, 23);
		panelSimulation.add(runSimulation);
		
		cancelSimulation = new JButton(" Cancel Simulation");
		cancelSimulation.setBounds(696, 92, 119, 23);
		panelSimulation.add(cancelSimulation);
		
		lblProfit = new JLabel("Profit :");
		lblProfit.setBounds(10, 213, 61, 20);
		panelSimulation.add(lblProfit);
		
		lbProfitResult = new JLabel("");
		lbProfitResult.setBounds(124, 213, 222, 20);
		panelSimulation.add(lbProfitResult);
		
		lblTrades = new JLabel("Trade list :");
		lblTrades.setBounds(10, 244, 61, 20);
		panelSimulation.add(lblTrades);
		
		lblBidID = new JLabel("Bid ID");
		lblBidID.setVerticalAlignment(SwingConstants.TOP);
		lblBidID.setBounds(124, 244, 222, 301);
		panelSimulation.add(lblBidID);
		
		lblAskID = new JLabel("Ask ID");
		lblAskID.setVerticalAlignment(SwingConstants.TOP);
		lblAskID.setBounds(370, 244, 222, 301);
		panelSimulation.add(lblAskID);
		
		lblPrice = new JLabel("Price");
		lblPrice.setVerticalAlignment(SwingConstants.TOP);
		lblPrice.setBounds(607, 244, 74, 298);
		panelSimulation.add(lblPrice);
		
		lblVolume = new JLabel("Volume");
		lblVolume.setVerticalAlignment(SwingConstants.TOP);
		lblVolume.setBounds(706, 244, 74, 298);
		panelSimulation.add(lblVolume);
		cancelSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// cancel the simulation (exit or remove entered input)
								
			}
		});
		
		runSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				factory = new Factory();
				CSV = Start.getFilePath(filePath.getText(), factory);
				String strat = (String) selectedStrategy.getSelectedItem();
				if (strat == "Mean Reversion" || strat == "Momentum" || strat == "Dumb" || strat == "Random") {
					selected = Start.selectStrategy(strat, factory);
				} else {
					System.err.println("Strategy is not selected");
					//update later to clear filepath and strategy option
					Start.exitProgram();
				}
				displayData.setText(filePath.getText());
				displayData.update(displayData.getGraphics());
				displayStrategy.setText(strat);
				displayStrategy.update(displayStrategy.getGraphics());
				Start.runSimulation(CSV, selected, factory);
			}
		});
		
	}
}
