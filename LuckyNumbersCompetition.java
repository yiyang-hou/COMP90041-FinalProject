import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 *         This class defines lucky numbers type competitions.
 */

public class LuckyNumbersCompetition extends Competition implements Serializable {
	/**
	 * Constructor. Passes in id, name and mode.
	 * 
	 * @param id
	 * @param name
	 * @param mode
	 */
	public LuckyNumbersCompetition(int id, String name, char mode, char type) {
		super(id, name, mode, type);
	}

	/**
	 * This method adds entries in to the competition.
	 */
	@Override
	public void addEntries(Scanner keyboard) {
		int totalEntries = 0;
		int manualEntries = 0;
		int autoEntries = 0;
		int tempNum = 0;
		String inputLine = "";
		int inputLength = 0;
		int[] inputNumbers = new int[NumbersEntry.ENTRY_LENGTH];

		ArrayList<Entry> tempEntryList = new ArrayList<Entry>();
		setSuccessAdd(true);

		Bill bill = billValidation(keyboard);
		double amount = bill.getAmount();
		totalEntries = (int) amount / AMOUNT_PER_ENTRY;

		if (totalEntries == 0) {
			System.out.println("This bill is not eligible for an entry."
					+ " The total amount is smaller than $50.0");
			setSuccessAdd(false);
			return;// No entry will be added.
		}

		System.out.println("This bill ($" + amount + ") is eligible for " + totalEntries
				+ " entries. How many manual entries did the customer fill up?: ");
		while (true) {
			manualEntries = keyboard.nextInt();
			keyboard.nextLine();

			if (manualEntries > totalEntries) {

				System.out.println("The number must be in the range from 0 to " + totalEntries
						+ ". Please try again.");

			} else
				break;
		}

		autoEntries = totalEntries - manualEntries;

		for (int mEntries = 0; mEntries < manualEntries; mEntries++) {
			// Take input and do input check.
			Outer_Loop: while (true) {
				try {
					System.out.println("Please enter 7 different numbers (from the range 1 to 35) "
							+ "separated by whitespace.");
					inputLine = keyboard.nextLine();
					StringTokenizer inputElement = new StringTokenizer(inputLine);
					inputLength = inputElement.countTokens();

					if (inputLength > NumbersEntry.ENTRY_LENGTH) {

						System.out.println("Invalid input! More than 7 numbers are provided. "
								+ "Please try again!");

					} else if (inputLength < NumbersEntry.ENTRY_LENGTH) {

						System.out.println("Invalid input! Fewer than 7 numbers are provided. "
								+ "Please try again!");

					} else {

						for (int i = 0; i < NumbersEntry.ENTRY_LENGTH; i++) {
							tempNum = Integer.parseInt(inputElement.nextToken());

							if (tempNum < 1 || tempNum > 35) {

								System.out
										.println("Invalid input! All numbers must be in the range "
												+ "from 1 to 35!");
								continue Outer_Loop;

							} else
								inputNumbers[i] = tempNum;
						}

						Arrays.sort(inputNumbers);

						for (int j = 0; j < NumbersEntry.ENTRY_LENGTH - 1; j++) {

							if (inputNumbers[j] == inputNumbers[j + 1]) {

								System.out.println(
										"Invalid input! All numbers must be " + "different!");
								break;

							}

							else if (j == NumbersEntry.ENTRY_LENGTH - 2) {
								break Outer_Loop;
							}
						}

					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid input! Numbers are expected. Please try again!");
				}
			}

			NumbersEntry manualEntry = new NumbersEntry(); // temporary object holder.
			manualEntry.createNumbers(inputNumbers);
			manualEntry.setMemberId(bill.getMemberId());
			manualEntry.setBillId(bill.getBillId());
			tempEntryList.add(manualEntry);
			setEntryNum(getEntryNum() + 1);

		}
		tempEntryList = addAutoEntries(autoEntries, bill.getMemberId(), bill.getBillId(),
				tempEntryList);
		setLatestEntriesInput(tempEntryList);
		assignEntryId(getLatestEntriesInput());
		getCEntryList().addAll(tempEntryList);
		bill.setIsUsed(true); // After successfully adding all entries, the bill becomes used.
	}

	/**
	 * This method is for adding automated entries.
	 * 
	 * @param autoEntries
	 * @param inputMemberId
	 * @param inputBillId
	 * @param tempEntryList
	 * @return
	 */
	public ArrayList<Entry> addAutoEntries(int autoEntries, String inputMemberId,
			String inputBillId, ArrayList<Entry> tempEntryList) {
		for (int aEntries = 0; aEntries < autoEntries; aEntries++) {
			AutoNumbersEntry autoEntry = new AutoNumbersEntry();

			if (getIsTestingMode()) {

				autoEntry.createNumbers(getEntryNum()); // Testing mode

			} else {

				autoEntry.createNumbers(); // Normal mode

			}

			autoEntry.setMemberId(inputMemberId);
			autoEntry.setBillId(inputBillId);
			tempEntryList.add(autoEntry);
			setEntryNum(getEntryNum() + 1);
		}
		return tempEntryList;
	}

	/**
	 * This method is for the automated drawing process.
	 * 
	 * @param id
	 * @return
	 */
	public AutoNumbersEntry drawLuckyNumbers(int id) {
		AutoNumbersEntry luckyEntry = new AutoNumbersEntry();

		if (getIsTestingMode()) {

			luckyEntry.createNumbers(id); // Testing mode

		} else {

			luckyEntry.createNumbers(); // Normal mode

		}
		return luckyEntry;
	}

	/**
	 * This method draws the winners.
	 */
	@Override
	public void drawWinners() {
		ArrayList<Entry> winningEntries = new ArrayList<Entry>();
		if (getCEntryList().size() == 0) {
			System.out.println("The current competition has no entries yet!");
		} else {
			System.out.println("Competition ID: " + getId() + ", Competition Name: " + getName()
					+ ", Type: LuckyNumbersCompetition");
			ArrayList<Entry> luckyDraw = new ArrayList<Entry>(1);
			luckyDraw.add(drawLuckyNumbers(getId()));
			setLatestEntriesInput(luckyDraw);
			assignEntryId(getLatestEntriesInput());
			setActive(false);
			System.out.print("Lucky Numbers:");
			luckyDraw.get(0).printNumbers();
			System.out.println();

			// turn Entry to NumbersEntry.
			ArrayList<NumbersEntry> competitionEntryList = new ArrayList<NumbersEntry>();
			for (Entry entry : getCEntryList()) {
				competitionEntryList.add((NumbersEntry) entry);
			}
			ArrayList<NumbersEntry> numLuckyDraw = new ArrayList<NumbersEntry>();
			numLuckyDraw.add((NumbersEntry) luckyDraw.get(0));

			for (NumbersEntry entry : competitionEntryList) {
				int prizeDivision = NumbersEntry.ENTRY_LENGTH + 1;
				for (int i = 0; i < NumbersEntry.ENTRY_LENGTH; i++) {
					for (int j = 0; j < NumbersEntry.ENTRY_LENGTH; j++) {
						if (entry.getNumbers()[i] == numLuckyDraw.get(0).getNumbers()[j]) {
							prizeDivision--;
						}
					}
				}
				if (prizeDivision <= 6) {
					entry.setPrizeDivision(prizeDivision);
					winningEntries.add(entry);
				}
			}
			finaliseWinners(winningEntries);
		}
	}

	/**
	 * This method prints out the message to display the winners.
	 */
	public void printWinners(int index) {
		setTotalPrize(getTotalPrize() + getWEntryList().get(index).getPrize());
		setWEntryNum(getWEntryNum() + 1);
		System.out.print("Member ID: " + getWEntryList().get(index).getMemberId()
				+ ", Member Name: "
				+ findParticipant(getWEntryList().get(index).getMemberId()).getMemberName());
		System.out.printf(", Prize: %-5d", getWEntryList().get(index).getPrize());
		System.out.println();
		System.out.print("--> Entry ID: " + getWEntryList().get(index).getEntryId() + ", Numbers:");
		getWEntryList().get(index).printNumbers();
		System.out.println();
	}
}
