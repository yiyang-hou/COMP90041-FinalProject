import java.io.Serializable;

/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 * 
 *         Bill class holds all bill properties for each bill entry. All Bill
 *         objects will be passed and collected in DataProvider class for later
 *         use. Getters for all properties and a setter for isUsed are required.
 */

public class Bill implements Serializable {
	private String memberId = "";
	private String billId = "";
	private double amount = 0;
	private boolean isUsed = false;

	public String getMemberId() {
		return memberId;
	}

	public String getBillId() {
		return billId;
	}

	public double getAmount() {
		return amount;
	}

	public boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(boolean newStatus) {
		this.isUsed = newStatus;
	}

	/**
	 * Pass the read data into a bill object.
	 * 
	 * @param memberId
	 * @param billId
	 * @param amount
	 * @param isUsed
	 */
	public Bill(String billId, String memberId, double amount, boolean isUsed) {
		this.billId = billId;
		this.memberId = memberId;
		this.amount = amount;
		this.isUsed = isUsed;
	}
}
