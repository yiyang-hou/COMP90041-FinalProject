import java.io.Serializable;

/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 *         This is the base class of all entries.
 */

public class Entry implements Serializable {
	private int entryId;
	private String billId;
	private String memberId;
	private int prizeDivision = 0;
	private int prize = 0;

	public void setPrize(int prize) {
		this.prize = prize;
	}

	/**
	 * This method is for randomPickCompetition. Because it sets prize directly
	 * without setting prizeDivision.
	 * 
	 * @return
	 */
	public int getPrizeForR() {
		return prize;
	}

	/**
	 * This method allows other classes to retrieve prize by division.
	 * 
	 * @return
	 */
	public int getPrize() {
		switch (prizeDivision) {
		case 1:
			this.prize = 50000;
			break;
		case 2:
			this.prize = 5000;
			break;
		case 3:
			this.prize = 1000;
			break;
		case 4:
			this.prize = 500;
			break;
		case 5:
			this.prize = 100;
			break;
		case 6:
			this.prize = 50;
			break;
		default:
			break;
		}
		return prize;
	}

	public void setPrizeDivision(int division) {
		this.prizeDivision = division;
	}

	public int getPrizeDivision() {
		return prizeDivision;
	}

	public void setEntryId(int newEntryId) {
		this.entryId = newEntryId;
	}

	public int getEntryId() {
		return this.entryId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillId() {
		return this.billId;
	}

	/**
	 * Print method, should do nothing for base Entry class.
	 */
	public void printNumbers() {
	}

}
