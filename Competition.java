
/**
 * @author Student name: Yiyang Hou
 * Student ID: 1202537
 * LMS username: yiyhou1
 * 
 * This is the abstract class for all types of competitions.
 */

import java.util.Scanner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Competition implements Serializable {
	private String name; // competition name
	private int id; // competition identifier
	private static DataProvider dataProvider = null;
	private boolean isTestingMode = false;
	private int totalPrize = 0;
	private int wEntryNum = 0; // The number of winning entries;
	private boolean active = true;
	private int entryNum = 0;
	private boolean successAdd = true;
	private ArrayList<Member> participants = new ArrayList<Member>();
	private char type = ' ';
	// magic numbers, they can be public because they are final.
	public final int ID_LENGTH = 6;
	public final int AMOUNT_PER_ENTRY = 50;

	private ArrayList<Entry> latestEntriesInput = new ArrayList<Entry>();// ArrayList of latest
																			// entry input.
	private ArrayList<Entry> cEntryList = new ArrayList<Entry>();// ArrayList of all entries in
																	// the competition.
	private ArrayList<Entry> wEntryList = new ArrayList<Entry>();// ArrayList of all winning
																	// entries.

	public void setWEntryList(ArrayList<Entry> setList) {
		this.wEntryList = setList;
	}

	public ArrayList<Entry> getWEntryList() {
		return this.wEntryList;
	}

	public void setLatestEntriesInput(ArrayList<Entry> setList) {
		this.latestEntriesInput = setList;
	}

	public ArrayList<Entry> getLatestEntriesInput() {
		return latestEntriesInput;
	}

	public ArrayList<Entry> getCEntryList() {
		return cEntryList;
	}

	public int getEntryNum() {
		return entryNum;
	}

	public void setEntryNum(int newEntryNum) {
		this.entryNum = newEntryNum;
	}

	public void setSuccessAdd(boolean isSuccessful) {
		this.successAdd = isSuccessful;
	}

	public boolean getSuccessAdd() {
		return successAdd;
	}

	public void setActive(boolean input) {
		active = input;
	}

	public boolean getActive() {
		return active;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public boolean getIsTestingMode() {
		return isTestingMode;
	}

	/**
	 * This method is set to be static is because the DataProvider object works for
	 * competition class itself.
	 * 
	 * @return
	 */
	public static DataProvider getDataProvider() {
		return dataProvider;
	}

	public void setTotalPrize(int newPrize) {
		totalPrize = newPrize;
	}

	public int getTotalPrize() {
		return totalPrize;
	}

	public void setWEntryNum(int newCount) {
		wEntryNum = newCount;
	}

	public int getWEntryNum() {
		return wEntryNum;
	}

	public char getType() {
		return type;
	}

	/**
	 * Each competition will be given an id and a name when created and will never
	 * be changed, therefore id and name are put in the parameter list of the
	 * constructor and no setters are needed.
	 * 
	 * @param id
	 * @param name
	 */
	public Competition(int id, String name, char mode, char type) {
		this.id = id;
		this.name = name;
		this.type = type;
		if (mode == 'T') {
			isTestingMode = true;
		} else
			isTestingMode = false;
	}

	public abstract void addEntries(Scanner keyboard);

	public abstract void drawWinners();

	/**
	 * This method retrieves an entry from a list.
	 * 
	 * @param entryId
	 * @param entryList
	 * @return
	 */
	public Entry getEntryFromList(int entryId, ArrayList<Entry> entryList) {
		int i = 0;

		while (i < entryList.size()) {
			if (entryId == entryList.get(i).getEntryId()) {
				break;
			}

			i++;
		}
		return entryList.get(i);
	}

	/**
	 * This method is used to retrieve the last entry id in the competition.
	 * 
	 * @return
	 */
	public int getCurrentIndex() {
		return cEntryList.size();
	}

	/**
	 * This method assigns a new id to each newly added entry in the competition.
	 * 
	 * @param newlyAdded
	 */
	public void assignEntryId(ArrayList<Entry> newlyAdded) {
		int id = getCurrentIndex();

		for (Entry eachEntry : newlyAdded) {
			eachEntry.setEntryId(id += 1);
		}
	}

	/**
	 * User input validation. Returns true if valid.
	 * 
	 * @param userInput
	 * @return
	 */
	public boolean userBillInputValidation(String userInput) {
		if (userInput.length() == ID_LENGTH) {
			for (int i = 0; i < ID_LENGTH; i++) {
				if (!Character.isDigit(userInput.charAt(i))) {
					System.out.println(
							"Invalid bill id! It must be a 6-digit number. " + "Please try again.");
					return false;
				}
			}
			return true;
		} else
			System.out.println(
					"Invalid bill id! It must be a 6-digit number." + " Please try again.");
		return false;
	}

	/**
	 * After checking for user input, this method checks validity of bill data.
	 * 
	 * @param keyboard
	 * @return
	 */
	public Bill billValidation(Scanner keyboard) {
		String inputBillId = "";
		Bill bill = null;
		Member validMember = null;
		boolean isValid = false;

		while (true) {
			isValid = false;
			while (isValid == false) {
				System.out.println("Bill ID: ");
				inputBillId = keyboard.nextLine();
				isValid = userBillInputValidation(inputBillId);
			}

			try {
				bill = getDataProvider().getBill(inputBillId);
				if (bill.getMemberId().equals("")) {
					System.out.println("This bill has no member id. Please try again.");
					continue;
				}
				validMember = getDataProvider().getMember(bill.getMemberId()); // Check if member
																				// exists.
				if (bill.getIsUsed()) {
					System.out.println(
							"This bill has already been used for a competition. Please try again.");
					continue;
				}

				break;
			} catch (DataAccessException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println("Something is wrong, please try try.");
			}
		}
		participants.add(validMember);
		return bill;
	}

	/**
	 * This method is defined to find a participant. Fatal error if no match is
	 * found and should kill the program.
	 * 
	 * @param memberId
	 * @return
	 */
	public Member findParticipant(String memberId) {
		while (true) {
			try {
				for (Member participant : participants) {
					if (participant.getMemberId().equals(memberId)) {
						return participant;
					}
				}
				throw new DataAccessException("The participant is missing! Abort!");
			} catch (DataAccessException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
		}
	}

	/**
	 * This method finalises the rewarding process for all valid winners and print
	 * the correct message. This method can be called directly in
	 * LuckyNumbersCompetition class, but needs to be overridden by
	 * RandomPickCompetition for its own implementation.
	 * 
	 * @param winningEntries
	 */
	public void finaliseWinners(ArrayList<Entry> winningEntries) {
		this.wEntryList = winningEntries;

		System.out.println("Winning entries:");

		Outer_Loop: for (int x = 0; x < wEntryList.size(); x++) {

			for (int y = 0; y < wEntryList.size(); y++) {

				if (wEntryList.get(x).getMemberId().equals(wEntryList.get(y).getMemberId())) {

					if (wEntryList.get(x).getPrizeDivision() > wEntryList.get(y)
							.getPrizeDivision()) {

						continue Outer_Loop;

					} else if (wEntryList.get(x).getPrizeDivision() == wEntryList.get(y)
							.getPrizeDivision()
							&& wEntryList.get(x).getEntryId() > wEntryList.get(y).getEntryId()) {

						continue Outer_Loop;

					}
				}
			}

			printWinners(x);
		}
	}

	public abstract void printWinners(int index);

	/**
	 * This method will print out info for all competitions.
	 */
	public void report() {
		System.out.println();
		if (active) {
			System.out.println("Competition ID: " + id + ", name: " + name + ", active: yes");
			System.out.println("Number of entries: " + entryNum);
		} else {
			System.out.println("Competition ID: " + id + ", name: " + name + ", active: no");
			System.out.println("Number of entries: " + entryNum);
			System.out.println("Number of winning entries: " + wEntryNum);
			System.out.println("Total awarded prizes: " + totalPrize);
		}
	}

	/**
	 * This method loads members files and bills files for all competitions
	 * 
	 * @param keyboard
	 */
	public static void loadFiles(Scanner keyboard) {
		System.out.println("Member file: ");
		String memberFile = keyboard.nextLine();
		System.out.println("Bill file: ");
		String billFile = keyboard.nextLine();

		try {
			dataProvider = new DataProvider(memberFile, billFile);
			// dataProvider contains all the members and bills, can be retrieved via
			// getMember()
			// and
			// getBill().
		} catch (DataAccessException e) {
			System.out.println("Data cannot be accessed! Program will be terminated!");
			System.exit(0);
		} catch (DataFormatException e) {
			System.out.println("Incorrect data format! Program will be terminated!");
			System.exit(0);
		}
	}

}
