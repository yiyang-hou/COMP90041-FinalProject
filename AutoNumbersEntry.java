
/**
 * @author Student name: Yiyang Hou
 * Student ID: 1202537
 * LMS username: yiyhou1
 * 
 * This class is for generating automatically picked numbers.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AutoNumbersEntry extends NumbersEntry implements Serializable {
	private final int NUMBER_COUNT = 7;
	private final int MAX_NUMBER = 35;

	/**
	 * This method generates completely random numbers.
	 * 
	 * @return
	 */
	public int[] createNumbers() {
		ArrayList<Integer> validList = new ArrayList<Integer>();
		int[] tempNumbers = new int[NUMBER_COUNT];
		for (int i = 1; i <= MAX_NUMBER; i++) {
			validList.add(i);
		}
		Collections.shuffle(validList);
		for (int i = 0; i < NUMBER_COUNT; i++) {
			tempNumbers[i] = validList.get(i);
		}
		Arrays.sort(tempNumbers);
		createNumbers(tempNumbers);
		return tempNumbers;
	}

	/**
	 * Automated generation method with control by seed.
	 * 
	 * @param seed
	 * @return
	 */
	public int[] createNumbers(int seed) {
		ArrayList<Integer> validList = new ArrayList<Integer>();
		int[] tempNumbers = new int[NUMBER_COUNT];
		for (int i = 1; i <= MAX_NUMBER; i++) {
			validList.add(i);
		}
		Collections.shuffle(validList, new Random(seed));
		for (int i = 0; i < NUMBER_COUNT; i++) {
			tempNumbers[i] = validList.get(i);
		}
		Arrays.sort(tempNumbers);
		createNumbers(tempNumbers);
		return tempNumbers;
	}

	/**
	 * Print numbers.
	 */
	@Override
	public void printNumbers() {
		super.printNumbers();
		System.out.print(" [Auto]");
	}
}
