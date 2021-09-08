
/**
 * @author Student name: Yiyang Hou
 * Student ID: 1202537
 * LMS username: yiyhou1
 * 
 * This class defines the properties for the all competitions.
 */

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.PrintWriter;

public class SimpleCompetitions implements Serializable {
	private Competition newCompetition = null;
	private ArrayList<Competition> competitionList = new ArrayList<Competition>();
	private char mode = ' ';

	public void setMode(char setMode) {
		this.mode = setMode;
	}

	public char getMode() {
		return this.mode;
	}

	public ArrayList<Competition> getCompetitionList() {
		return competitionList;
	}

	public Competition getNewCompetition() {
		return newCompetition;
	}

	/**
	 * This method checks whether there is an ongoing active competition.
	 * 
	 * @return
	 */
	public boolean activeCompetitionCheck() {
		if (this.newCompetition != null && this.newCompetition.getActive()) {
			return true;
		} else
			return false;
	}

	/**
	 * This method adds new a competition. the new competition is automatically set
	 * to be active.
	 * 
	 * @param newCompetitionName
	 * @return
	 */
	public Competition addNewCompetition(String newCompetitionName, String type) {
		int id = competitionList.size() + 1;
		if (type.equals("L")) {
			this.newCompetition = new LuckyNumbersCompetition(id, newCompetitionName, this.mode,
					type.charAt(0));
			System.out.println("A new competition has been created!");
			System.out.println("Competition ID: " + newCompetition.getId() + ", Competition Name: "
					+ newCompetition.getName() + ", Type: LuckyNumbersCompetition");
		}
		if (type.equals("R")) {
			this.newCompetition = new RandomPickCompetition(id, newCompetitionName, this.mode,
					type.charAt(0));
			System.out.println("A new competition has been created!");
			System.out.println("Competition ID: " + newCompetition.getId() + ", Competition Name: "
					+ newCompetition.getName() + ", Type: RandomPickCompetition");
		}
		competitionList.add(newCompetition);
		return newCompetition;
	}

	/**
	 * This method is responsible for the report option in the menu.
	 */
	public void report() {
		int compNum = getCompetitionList().size();
		int activeNum = 0;
		if (getNewCompetition().getActive()) {
			compNum -= 1;
			activeNum = 1;
		}

		System.out.println("----SUMMARY REPORT----");
		System.out.println("+Number of completed competitions: " + compNum);
		System.out.println("+Number of active competitions: " + activeNum);
		for (Competition competition : getCompetitionList()) {
			competition.report();
		}
	}

	/**
	 * This method writes the existing competitions to a binary file, as well as
	 * updating the bills.csv file.
	 * 
	 * @param binaryFile
	 */
	public void writeToFile(String binaryFile) {
		String billId = "";
		String memberId = "";
		double amount = 0;
		String isUsedString = "";
		ArrayList<Bill> billList = Competition.getDataProvider().getBillList();

		String billLine = "";
		ArrayList<String> billLines = new ArrayList<String>();

		for (int i = 0; i < billList.size(); i++) {
			billId = billList.get(i).getBillId();
			memberId = billList.get(i).getMemberId();
			amount = billList.get(i).getAmount();
			if (billList.get(i).getIsUsed()) {
				isUsedString = "TRUE";
			} else {
				isUsedString = "FALSE";
			}

			billLine = billId + "," + memberId + "," + Double.toString(amount) + "," + isUsedString;
			billLines.add(billLine);
		}

		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					new FileOutputStream(binaryFile));
			outputStream.writeObject(this);

			PrintWriter writer = new PrintWriter(new FileOutputStream("bills.csv"));
			for (int i = 0; i < billList.size(); i++) {
				writer.println(billLines.get(i));
			}
			outputStream.close();
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File cannot be found! Program will be terminated!");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(
					"Something went wrong when writing to the file! Program will be terminated!");
			System.exit(0);
		}
		System.out.println("Competitions have been saved to file.");
		System.out.println("The bill file has also been automatically updated.");
	}

	/**
	 * Main program that uses the main SimpleCompetitions class
	 * 
	 * @param args main program arguments
	 */
	public static void main(String[] args) {

		// Create an object of the SimpleCompetitions class
		SimpleCompetitions sc = new SimpleCompetitions();

		// Add your code to complete the task

		Scanner keyboard = new Scanner(System.in); // The only scanner object for keyboard input.

		// Program start up stage.
		System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----");
		outerLoop: while (true) {
			System.out.println("Load competitions from file? (Y/N)?");
			String userInput = keyboard.nextLine();
			char ifLoad = userInput.toUpperCase().charAt(0);
			if (userInput.length() == 1 && (ifLoad == 'Y' || ifLoad == 'N')) {
				if (ifLoad == 'N') {

					while (true) {
						System.out.println("Which mode would you like to run? (Type T for Testing, "
								+ "and N for Normal mode):");
						userInput = keyboard.nextLine();
						char mode = userInput.toUpperCase().charAt(0);
						if (userInput.length() == 1 && (mode == 'T' || mode == 'N')) {
							sc.setMode(mode);
							break outerLoop;
						} else {
							System.out.println("Invalid mode! Please choose again.");
						}
					}

				} else {
					System.out.println("File name:");
					userInput = keyboard.nextLine();
					DataProvider competitions = new DataProvider(userInput);
					sc = competitions.getSc();
					break outerLoop;
				}
			} else {
				System.out.println("Unsupported option. Please try again!");
			}
		}

		Competition.loadFiles(keyboard); // Load members and bills from files.

		// Main menu starts from here.
		mainLoop: while (true) {
			int option = 0;
			boolean done = false;
			while (!done) {
				System.out.println("Please select an option. Type 5 to exit.");
				System.out.println("1. Create a new competition");
				System.out.println("2. Add new entries");
				System.out.println("3. Draw winners");
				System.out.println("4. Get a summary report");
				System.out.println("5. Exit");

				try {
					option = keyboard.nextInt();
					done = true;
				} catch (InputMismatchException e) {
					System.out.println("A number is expected. Please try again.");
				} finally {
					keyboard.nextLine();
				}
			}

			switch (option) {

			case 1:
				String inputType = "";
				if (sc.activeCompetitionCheck()) {
					System.out.println("There is an active competition. SimpleCompetitions "
							+ "does not support concurrent competitions!");
					break;
				}

				while (true) {
					System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
					inputType = keyboard.nextLine().toUpperCase();
					if (inputType.length() != 1
							|| !(inputType.equals("L") || inputType.equals("R"))) {
						System.out.println("Invalid competition type! Please choose again.");
					} else {
						break;
					}
				}

				System.out.println("Competition name: ");
				String newCompetitionName = keyboard.nextLine();
				sc.addNewCompetition(newCompetitionName, inputType);
				break;

			case 2:
				if (!sc.activeCompetitionCheck()) {
					System.out.println("There is no active competition. Please create one!");
					break;
				}

				Outer_Loop1: while (true) {
					sc.getNewCompetition().addEntries(keyboard);
					if (sc.getNewCompetition().getSuccessAdd()) {
						if (sc.getNewCompetition().getType() == 'L') {
							System.out.println("The following entries have been added:");
							for (Entry eachEntry : sc.getNewCompetition().getLatestEntriesInput()) {
								System.out.printf("Entry ID: %-7dNumbers:", eachEntry.getEntryId());
								eachEntry.printNumbers();
								System.out.println();
							}
						} else {
							System.out.println(
									"The following entries have been automatically generated:");
							for (Entry eachEntry : sc.getNewCompetition().getLatestEntriesInput()) {
								System.out.printf("Entry ID: %-6d", eachEntry.getEntryId());
								System.out.println();
							}
						}
					}

					while (true) {
						System.out.println("Add more entries (Y/N)?");
						char ifMore = keyboard.next().toUpperCase().charAt(0);
						keyboard.nextLine();
						if (ifMore == 'N') {
							break Outer_Loop1;
						} else if (ifMore != 'Y') {
							System.out.println("Unsupported option. Please try again!");
						} else
							break;
					}
				}
				break;

			case 3:
				if (!sc.activeCompetitionCheck()) {
					System.out.println("There is no active competition. Please create one!");
					break;
				}

				sc.getNewCompetition().drawWinners();
				break;

			case 4:
				if (sc.getCompetitionList().size() == 0) {
					System.out.println("No competition has been created yet!");
					break;
				}
				sc.report();
				break;

			case 5:
				while (true) {
					System.out.println("Save competitions to file? (Y/N)?");
					char save = keyboard.next().toUpperCase().charAt(0);
					keyboard.nextLine();
					if (save == 'N') {
						break mainLoop;
					} else if (save != 'Y') {
						System.out.println("Unsupported option. Please try again!");
					} else {

						System.out.println("File name:");
						String binaryFile = keyboard.nextLine();
						sc.writeToFile(binaryFile);
						break mainLoop;
					}
				}

			default:
				System.out.println("Unsupported option. Please try again!");
				break;
			}
		}

		System.out.println("Goodbye!");
		keyboard.close();
	}
}
