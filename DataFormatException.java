/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 *         This class is used to define data format exceptions.
 */

public class DataFormatException extends Exception {

	/**
	 * This construction is for default message.
	 */
	public DataFormatException() {
		super("Incorrect data format!");
	}

	/**
	 * This construction is for customised message.
	 */
	public DataFormatException(String message) {
		super(message);
	}
}
