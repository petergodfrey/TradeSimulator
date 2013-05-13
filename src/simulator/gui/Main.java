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
	JLabel displayData;
	JLabel displayStrategy;
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
		
		String[] strategyOptions = {"Select one of strategies", "Mean Reversion", "Momentum"};
		
		panelSimulation = new JPanel();
		panelSimulation.setBounds(0, 0, 825, 556);
		contentPane.add(panelSimulation);
		panelSimulation.setLayout(null);
		
		lblSelectedDateFile = new JLabel("Selected Date File :");
		lblSelectedDateFile.setBounds(10, 119, 104, 20);
		panelSimulation.add(lblSelectedDateFile);
		
		lblSelectedStrategy = new JLabel("Selected Strategy :");
		lblSelectedStrategy.setBounds(10, 151, 104, 20);
		panelSimulation.add(lblSelectedStrategy);
		
		lblProgress = new JLabel("Progress :");
		lblProgress.setBounds(10, 182, 104, 20);
		panelSimulation.add(lblProgress);
		
		displayData = new JLabel("");
		displayData.setBounds(124, 119, 691, 20);
		panelSimulation.add(displayData);
		
		displayStrategy = new JLabel("");
		displayStrategy.setBounds(124, 151, 691, 20);
		panelSimulation.add(displayStrategy);
		
		lblDataFile = new JLabel("Data File Path :");
		lblDataFile.setBounds(10, 11, 74, 14);
		panelSimulation.add(lblDataFile);
		
		
		filePath = new JTextField();
		filePath.setBounds(124, 11, 683, 20);
		panelSimulation.add(filePath);
		filePath.setText("Enter the data file path");
		filePath.setToolTipText("For example, \"C:\\User\\user\\Desktop\\sircaData.csv");
		filePath.setColumns(10);
		
		lblStrategy = new JLabel("Strategy :");
		lblStrategy.setBounds(10, 38, 49, 14);
		panelSimulation.add(lblStrategy);
		selectedStrategy = new JComboBox (strategyOptions);
		selectedStrategy.setBounds(124, 39, 176, 20);
		panelSimulation.add(selectedStrategy);
		selectedStrategy.setSelectedIndex(0);
		selectedStrategy.setToolTipText("Click the arrow and select one of strategies");
		selectedStrategy.setMaximumRowCount(3);
		
		progressPercent = new JProgressBar();
		progressPercent.setIndeterminate(true);
		progressPercent.setBounds(124, 184, 691, 20);
		panelSimulation.add(progressPercent);
		progressPercent.setStringPainted(true);
				
				runSimulation = new JButton("Run Simulation");
				runSimulation.setBounds(590, 92, 103, 23);
				panelSimulation.add(runSimulation);
				
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

						Start.runSimulation(CSV, selected, factory);
						System.out.println("I'm end of mouseClicked()");
					}
				});
		
				cancelSimulation = new JButton(" Cancel Simulation");
				cancelSimulation.setBounds(696, 92, 119, 23);
				panelSimulation.add(cancelSimulation);
				cancelSimulation.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// cancel the simulation (exit or remove entered input)
										
					}
				});

		
	}
}
