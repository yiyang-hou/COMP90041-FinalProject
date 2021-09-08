import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 *         This class defines random pick type competitions.
 */

public class RandomPickCompetition extends Competition implements Serializable {
	private final int FIRST_PRIZE = 50000;
	private final int SECOND_PRIZE = 5000;
	private final int THIRD_PRIZE = 1000;
	private final int[] prizes = { FIRST_PRIZE, SECOND_PRIZE, THIRD_PRIZE };

	private final int MAX_WINNING_ENTRIES = 3;

	/**
	 * Constructor. Passes in id, name and mode.
	 * 
	 * @param id
	 * @param name
	 * @param mode
	 */
	public RandomPickCompetition(int id, String name, char mode, char type) {
		super(id, name, mode, type);
	}

	/**
	 * This method adds entries in to the competition.
	 */
	@Override
	public void addEntries(Scanner keyboard) {
		int totalEntries = 0;
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

		System.out.println(
				"This bill ($" + amount + ") is eligible for " + totalEntries + " entries.");

		for (int i = 0; i < totalEntries; i++) {
			Entry basicEntry = new Entry();
			basicEntry.setBillId(bill.getBillId());
			basicEntry.setMemberId(bill.getMemberId());
			tempEntryList.add(basicEntry);
			setEntryNum(getEntryNum() + 1);
		}
		setLatestEntriesInput(tempEntryList);
		assignEntryId(getLatestEntriesInput());
		getCEntryList().addAll(tempEntryList);
		bill.setIsUsed(true); // After successfully adding all entries, the bill becomes used.
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
					+ ", Type: RandomPickCompetition");
			Random randomGenerator = null;
			if (this.getIsTestingMode()) {
				randomGenerator = new Random(this.getId());
			} else {
				randomGenerator = new Random();
			}

			int winningEntryCount = 0;
			while (winningEntryCount < MAX_WINNING_ENTRIES) {
				int winningEntryIndex = randomGenerator.nextInt(getCEntryList().size());

				Entry winningEntry = getCEntryList().get(winningEntryIndex);

				/*
				 * Ensure that once an entry has been selected, it will not be selected again.
				 */
				if (winningEntry.getPrize() == 0) {
					int currentPrize = prizes[winningEntryCount];
					winningEntry.setPrize(currentPrize);
					winningEntryCount++;
					winningEntries.add(winningEntry);
				}
			}
			setActive(false);
			
			//This part sorts the entries in entry ID;
			ArrayList<Entry> sortedWinningEntries = new ArrayList<Entry>();
			int[] entryIds = new int[3];
			for (int i = 0; i < MAX_WINNING_ENTRIES; i++) {
				entryIds[i] = winningEntries.get(i).getEntryId();
			}
			Arrays.sort(entryIds);
			for (int i = 0; i < MAX_WINNING_ENTRIES; i++) {
				sortedWinningEntries.add(getEntryFromList(entryIds[i], winningEntries));
			}

			/*
			 * Note that the above piece of code does not ensure that one customer gets at
			 * most one winning entry. Add your code to complete the logic.
			 */
			finaliseWinners(sortedWinningEntries);
		}
	}

	/**
	 * This method picks all valid winners and print.
	 * 
	 * @param winningEntries
	 */
	@Override
	public void finaliseWinners(ArrayList<Entry> winningEntries) {
		setWEntryList(winningEntries);
		System.out.println("Winning entries:");
		Outer_Loop: for (int x = 0; x < getWEntryList().size(); x++) {
			for (int y = 0; y < getWEntryList().size(); y++) {
				if (getWEntryList().get(x).getMemberId()
						.equals(getWEntryList().get(y).getMemberId())) {
					if (getWEntryList().get(x).getPrizeForR() < getWEntryList().get(y)
							.getPrizeForR()) {
						continue Outer_Loop;
					} else if (getWEntryList().get(x).getPrizeForR() == getWEntryList().get(y)
							.getPrizeForR()
							&& getWEntryList().get(x).getEntryId() > getWEntryList().get(y)
									.getEntryId()) {
						continue Outer_Loop;
					}
				}
			}

			printWinners(x);
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
		System.out.print(", Entry ID: " + getWEntryList().get(index).getEntryId());
		System.out.printf(", Prize: %-5d", getWEntryList().get(index).getPrize());
		System.out.println();
	}
}
