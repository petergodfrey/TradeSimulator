package simulator;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Run {

	public static void main (String[] args) {
		System.out.println("#######--Welcome to the Trade Simulator!--#######");
		//menu holds CSV file and strategy selected until running time
		Reader CSV = null;
		Strategy strat = null;
		//one scanner that is to be passed throughout the menu when
		//input is needed, prevents memory leak problems from too many scanners
		Scanner s = new Scanner(System.in);
		boolean infLoop = true;
		//loops indefinitely until exitProgram function is run
		while (infLoop) {
			int choice = menu(CSV, strat, s);
			switch (choice) {
			case 1: 
				CSV = selectDataFile(s);
				break;
			case 2: 
				strat = selectStrategy(s);
				break;
			case 3: 
				runSimulation(CSV, strat);
				break;
			case 4: 
				exitProgram(s);
				break;
			}
		}
	}

	private static int menu (Reader CSV, Strategy strat, Scanner s) {
		{//displays currently selected options in menu
			System.out.print("\nCurrent selected CSV:\t\t");
			if (CSV == null) {
				System.out.print("None");
			} else {
				System.out.print(CSV.getFilePath());
			}
			System.out.println();
			System.out.print("Current selected Strategy:\t");
			if (strat == null) {
				System.out.print("None");
			} else {
				System.out.print(strat.getStrategyName());
			}
			System.out.printf("\n\n");
		}
		System.out.println("Please select an option below");
		System.out.println("1 - Select Data File");
		System.out.println("2 - Select Strategy");
		System.out.println("3 - Run Simulation");
		System.out.println("4 - Exit Program");
		System.out.println("Please select a number (1 - 4)");

		int choice = 0;
		boolean validInput = false;
		while (validInput == false) {// loop to repeat until there is a valid input
			try {
				choice = s.nextInt();
				if (choice < 1 || choice > 4) {
					throw new InputMismatchException();
				}
				validInput = true;
			} catch (InputMismatchException e) {
				//exception catches everything except 1-4
				System.out.println("Wrong input, select a number between 1 and 4");
				s.nextLine();
			}
		}
		return choice;
	}

	private static Reader selectDataFile(Scanner s) {
		System.out.println("Type the filepath of the selected CSV file");
		Reader CSV = null;
		String filepath = s.next();
		try {
			CSV = new Reader(filepath);
		} catch (Exception e) {
			System.out.printf("Invalid filepath, returning to menu\n\n");
		}
		return CSV;
	}
	private static Strategy selectStrategy(Scanner s) {
		//must find a way to automate the list menu, currently must
		//manually add them in
		System.out.println("Select a strategy from the list");
		System.out.println("1 - No Strategy");
		System.out.println("2 - Dumb Strategy");
		Strategy strat = null;
		try {
			int choice = s.nextInt();
			if (choice < 1 || choice > 2) {
				throw new InputMismatchException();
			}
			switch (choice) {
			case 1:
				strat = null;
				break;
			case 2:
				strat = new DumbStrategy(null);
				break;
			}
		} catch (InputMismatchException e) {
			System.out.println("Wrong input, returning to menu\n\n");
		}
		return strat;
	}
	
	private static void runSimulation(Reader CSV, Strategy strat) {
        // Create the objects required by the SignalGenerator
        OrderBooks  orderBooks  = new OrderBooks("MQG");
        TradeEngine tradeEngine = new TradeEngine(orderBooks);

        SignalGenerator signalGenerator = new SignalGenerator(CSV, strat, orderBooks, tradeEngine);

        System.out.print("Running simulation........... ");

        while (signalGenerator.advance() != SignalGenerator.SIMULATION_END){
            // Do nothing
        }
        System.out.println("Finished");
	}
	private static void exitProgram(Scanner s) {
		//exits the program in a neat manner
		System.out.println("Cya!");
		s.close();
		System.exit(0);
	}



}