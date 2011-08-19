package eu.smoothit.sis.controller.iop;

/**
 * A structure for holding the local IP ranges to be sent to the IoP.
 * This object is obsolete and will be deleted in next version.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class IPRange {
	
	/**
	 * The IP prefix, in format "xx.xx.xx.xx".
	 */
	protected String prefix;
	
	/**
	 * The IP prefix length.
	 */
	protected int prefix_length;

	/**
	 * Gets the IP prefix.
	 * 
	 * @return The IP prefix.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the IP prefix.
	 * 
	 * @param prefix The IP prefix.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Gets the IP prefix length.
	 * 
	 * @return The IP prefix length.
	 */
	public int getPrefix_length() {
		return prefix_length;
	}

	/**
	 * Sets the IP prefix length.
	 * 
	 * @param prefixLength The IP prefix length.
	 */
	public void setPrefix_length(int prefixLength) {
		prefix_length = prefixLength;
	}
}
