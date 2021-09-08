import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 *         This class is for all entries with numbers.
 */

public class NumbersEntry extends Entry implements Serializable {
	public static final int ENTRY_LENGTH = 7; // This is a constant, so can be set public and is set
												// to static as this property holds for all number
												// entries.

	private int[] numbers = new int[ENTRY_LENGTH];

	public int[] getNumbers() {
		return numbers;
	}

	/**
	 * Create numbers for the entry.
	 * 
	 * @param inputNumbers
	 */
	public void createNumbers(int[] inputNumbers) {
		for (int i = 0; i < ENTRY_LENGTH; i++) {
			numbers[i] = inputNumbers[i];
		}
		Arrays.sort(numbers);
	}

	/**
	 * Print numbers.
	 */
	@Override
	public void printNumbers() {
		for (int number : numbers) {
			if (number < 10) {
				System.out.print("  " + number);
			} else {
				System.out.print(" " + number);
			}
		}
	}
}
