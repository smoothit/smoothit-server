/**
 * 
 */
package eu.smoothit.sis.db.impl.utils;

/**
 * @author Christian Gross
 * 
 */
public class DBNotifyMessage {

	public static enum ACTION {
		PERSIST, UPDATE, REMOVE

	};

	private ACTION a;

	private String source;

	/**
	 * constructor for the DBNotifyMessage
	 * 
	 * @param source
	 *            the DAO which has changed
	 * @param a
	 *            the action performed on the DAO
	 */
	public DBNotifyMessage(String source, ACTION a) {

		this.source = source;
		this.a = a;

	}

	public ACTION getAction() {
		return a;
	}

	public String getSource() {
		return source;
	}

}
