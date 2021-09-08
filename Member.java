import java.io.Serializable;

/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 * 
 *         Member class holds all properties for each member. All Member objects
 *         will be passed and collected in DataProvider class for later use.
 *         Only getters are required.
 */

public class Member implements Serializable {
	private String memberId = "";
	private String memberName = "";
	// member Email is omitted.

	public String getMemberId() {
		return memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	/**
	 * Pass the read data into a member object.
	 * 
	 * @param memberId
	 * @param memberName
	 */
	public Member(String memberId, String memberName) {
		this.memberId = memberId;
		this.memberName = memberName;
	}

}
