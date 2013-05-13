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

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField filePath;
	private JPanel panelInput;
	private JPanel panelSimulation;
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
	JLabel displayData;
	JLabel displayStrategy;
	JComboBox<String> selectedStrategy;
	JButton runSimulation;
	JButton cancelSimulation;
	JProgressBar progressPercent;
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
		runSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// run the simulation (pass the info to Run class)
/*
				Factory factory = new Factory();
				Reader CSV = Start.getFilePath(filePath.getText(), factory);
				Strategy selected = null;
*/				
				factory = new Factory();
				CSV = Start.getFilePath(filePath.getText(), factory);
				String strat = (String) selectedStrategy.getSelectedItem();
				if (strat == "Mean Reversion" || strat == "Momentum") {
					selected = Start.selectStrategy(strat, factory);
				} else {
					System.err.println("Strategy is not selected");
					//update later to clear filepath and strategy option
					Start.exitProgram();
				}
				panelInput.setOpaque(false);
				lblDataFile.setOpaque(false);
				lblStrategy.setOpaque(false);
				selectedStrategy.setOpaque(false);
				filePath.setOpaque(false);
				cancelSimulation.setOpaque(false);
				runSimulation.setOpaque(false);
							
				panelSimulation.setOpaque(true);
				progressPercent.setOpaque(true);
				lblSelectedDateFile.setOpaque(true);
				lblSelectedStrategy.setOpaque(true);
				lblProgress.setOpaque(true);
				displayData.setOpaque(true);
				displayStrategy.setOpaque(true);
			}
		});
		System.out.print("Check Check Check!!!");
		System.out.println(Start.getProgress(CSV));
		progressPercent.setString(Start.getProgress(CSV));
		Start.runSimulation(CSV, selected, factory);
		//System.out.println("I'm end of mouseClicked()");
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
		
		panelInput = new JPanel();
		panelInput.setBounds(0, 0, 825, 110);
		contentPane.add(panelInput);
		panelInput.setLayout(null);
		
		lblStrategy = new JLabel("Strategy :");
		lblStrategy.setBounds(10, 34, 49, 14);
		panelInput.add(lblStrategy);
		
		lblDataFile = new JLabel("Data File Path :");
		lblDataFile.setBounds(10, 9, 74, 14);
		panelInput.add(lblDataFile);
		
		String[] strategyOptions = {"Select one of strategies", "Mean Reversion", "Momentum"};
		selectedStrategy = new JComboBox<String>(strategyOptions);
		selectedStrategy.setSelectedIndex(0);
		selectedStrategy.setBounds(104, 31, 176, 20);
		panelInput.add(selectedStrategy);
		selectedStrategy.setToolTipText("Click the arrow and select one of strategies");
		selectedStrategy.setMaximumRowCount(3);
		
		
		filePath = new JTextField();
		filePath.setBounds(104, 6, 683, 20);
		panelInput.add(filePath);
		filePath.setText("Enter the data file path");
		filePath.setToolTipText("For example, \"C:\\User\\user\\Desktop\\sircaData.csv");
		filePath.setColumns(10);

		cancelSimulation = new JButton(" Cancel Simulation");
		cancelSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// cancel the simulation (exit or remove entered input)
								
			}
		});		

		cancelSimulation.setBounds(668, 78, 119, 23);
		panelInput.add(cancelSimulation);	
		
		panelSimulation = new JPanel();
		panelSimulation.setBounds(0, 0, 825, 556);
		contentPane.add(panelSimulation);
		panelSimulation.setLayout(null);
		panelSimulation.setOpaque(false);
		
		progressPercent = new JProgressBar();
		progressPercent.setStringPainted(true);
		progressPercent.setBounds(124, 67, 691, 20);
		progressPercent.setOpaque(false);
		panelSimulation.add(progressPercent);
		
		lblSelectedDateFile = new JLabel("Selected Date File :");
		lblSelectedDateFile.setBounds(10, 11, 104, 20);
		lblSelectedDateFile.setOpaque(false);
		panelSimulation.add(lblSelectedDateFile);
		
		lblSelectedStrategy = new JLabel("Selected Strategy :");
		lblSelectedStrategy.setBounds(10, 38, 104, 20);
		lblSelectedStrategy.setOpaque(false);
		panelSimulation.add(lblSelectedStrategy);
		
		lblProgress = new JLabel("Progress :");
		lblProgress.setBounds(10, 67, 104, 20);
		lblProgress.setOpaque(false);
		panelSimulation.add(lblProgress);
		
		displayData = new JLabel("");
		displayData.setBounds(124, 11, 691, 20);
		displayData.setOpaque(false);
		panelSimulation.add(displayData);
		
		displayStrategy = new JLabel("");
		displayStrategy.setBounds(124, 38, 691, 20);
		displayStrategy.setOpaque(false);
		panelSimulation.add(displayStrategy);		
		
		runSimulation = new JButton("Run Simulation");
		runSimulation.setBounds(555, 78, 103, 23);
		panelInput.add(runSimulation);
	}
}
