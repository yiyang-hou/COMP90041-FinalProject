import java.util.Scanner;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.*;

/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 *         The DataProvider Class stores all existing competitions, a list of
 *         members and a list of bills that are read from the files. The main
 *         program will get data from this class.
 * 
 */

public class DataProvider implements Serializable {
	private ArrayList<Member> memberList = new ArrayList<Member>();
	private ArrayList<Bill> billList = new ArrayList<Bill>();
	SimpleCompetitions loadedSc = null;

	/**
	 * This method returns a member object by selecting a member id.
	 * 
	 * @param memberId
	 * @return
	 */
	public Member getMember(String memberId) throws DataAccessException {
		int i = 0;
		while (i < memberList.size()) {
			if (memberId.equals(memberList.get(i).getMemberId())) {
				break;
			}
			i++;
		}

		if (i == memberList.size()) {
			throw new DataAccessException("This member does not exist. Please try again.");
		}
		return memberList.get(i);
	}

	/**
	 * This method returns a bill object by selecting a bill id.
	 * 
	 * @param billId
	 * @return
	 */
	public Bill getBill(String billId) throws DataAccessException {
		int i = 0;

		while (i < billList.size()) {
			if (billId.equals(billList.get(i).getBillId())) {
				break;
			}
			i++;
		}

		if (i == billList.size()) {
			throw new DataAccessException("This bill does not exist. Please try again.");
		}
		return billList.get(i);
	}

	public ArrayList<Bill> getBillList() {
		return billList;
	}

	public SimpleCompetitions getSc() {
		return loadedSc;
	}

	/**
	 * This constructor will be called first if user wants to load existing
	 * competitions.
	 * 
	 * @param binaryFile
	 */
	public DataProvider(String binaryFile) {
		try {

			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(binaryFile));

			try {

				while (true) {
					loadedSc = (SimpleCompetitions) inputStream.readObject();
				}

			} catch (EOFException e) { // Check for the end of the file.
			} catch (ClassNotFoundException e) {
				System.out.println("Error when loading competitions. Program will be terminated!");
				System.exit(0);
			}

			inputStream.close();

		} catch (FileNotFoundException e) {
			System.out.println("File cannot be found! Program will be terminated!");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(
					"Something went wrong when reading from the file! Program will be terminated!");
			System.exit(0);
		}
	}

	/**
	 * The constructor reads the member file and bill file.
	 * 
	 * @param memberFile A path to the member file (e.g., members.csv)
	 * @param billFile   A path to the bill file (e.g., bills.csv)
	 * @throws DataAccessException If a file cannot be opened/read
	 * @throws DataFormatException If the format of the the content is incorrect
	 */
	public DataProvider(String memberFile, String billFile)
			throws DataAccessException, DataFormatException { // These exceptions will be handled
																// at the invocation.
		String memberLine = "";
		String billLine = "";
		Scanner inputStream = null;
		String memberId = "";
		String memberName = "";
		String billId = "";
		double amount = 0;
		boolean isUsed = false;

		try {
			// Read from a member file.
			inputStream = new Scanner(new FileInputStream(memberFile));
			while (inputStream.hasNextLine()) {
				memberLine = "";
				memberId = "";
				memberName = "";

				memberLine = inputStream.nextLine();
				String[] memberCols = memberLine.split(",");

				if (memberCols.length != 3) {
					throw new InputMismatchException();
				}

				memberId = memberCols[0];
				memberName = memberCols[1];
				Member member = new Member(memberId, memberName);
				memberList.add(member);
			}
			inputStream.close();

			// Read from a bill file.
			inputStream = new Scanner(new FileInputStream(billFile));
			while (inputStream.hasNextLine()) {
				billLine = "";
				billId = "";
				memberId = "";
				amount = 0;

				billLine = inputStream.nextLine();
				String[] billCols = billLine.split(",");

				if (billCols.length != 4) {
					inputStream.close();
					throw new InputMismatchException();
				}

				billId = billCols[0];
				memberId = billCols[1];
				amount = Double.parseDouble(billCols[2]);
				String isUsedString = billCols[3].toLowerCase();

				if (isUsedString.equals("false")) {
					isUsed = false;
				} else if (isUsedString.equals("true")) {
					isUsed = true;
				} else {
					inputStream.close();
					throw new InputMismatchException();
				}

				Bill bill = new Bill(billId, memberId, amount, isUsed);
				billList.add(bill);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("File cannot be found! Program will be terminated!");
			System.exit(0);
		} catch (InputMismatchException e) {
			System.out.println("File includes invalid data! Program will be terminated!");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Something went wrong! Program will be terminated!");
			System.exit(0);
		}
	}
}
