/**
 * @author Student name: Yiyang Hou Student ID: 1202537 LMS username: yiyhou1
 * 
 *         This class is used to define data access exceptions.
 */

public class DataAccessException extends Exception {

	/**
	 * This construction is for default message.
	 */
	public DataAccessException() {
		super("Data can not be accessed!");
	}

	/**
	 * This construction is for customised message.
	 */
	public DataAccessException(String message) {
		super(message);
	}
}
