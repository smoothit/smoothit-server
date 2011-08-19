package eu.smoothit.sis.controller.iop;

/**
 * A structure for holding the configuration parameters for the IoP.
 * See IoP design document for more details.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class ConfigParams {
	
	/**
	 * The operation mode of the IoP.
	 */
	protected String mode;
	
	/**
	 * A flag denoting whether the IoP should connect to remote peers or not.
	 */
	protected boolean remotes;
	
	/**
	 * The number of unchoking slots per swarm.
	 */
	protected int slots;
	
	/**
	 * The list with the IP ranges of the local addresses. 
	 * Each range is expected to be in the form "xx.xx.xx.xx/yy".
	 *
	 */
	protected String[] localIPRanges;
	
	/**
	 * The time period (in seconds) to ask the SIS for new torrent statistics.
	 */
	protected int t;
	
	/**
	 * Lower bound for upload bandwidth (in Mbit/s).
	 */
	protected float ulow;
	
	/**
	 * Lower bound for download bandwidth (in Mbit/s).
	 */
	protected float dlow;
	
	/**
	 * The total upload bandwidth (in Mbit/s).
	 */
	protected float u;
	
	/**
	 * The total download bandwidth (in Mbit/s).
	 */
	protected float d;
	
	/**
	 * The percentage (%) of old swarms the IoP will keep participating in.
	 */
	protected float x;

	/**
	 * Gets the operating mode of the IoP.
	 * 
	 * @return The operating mode of the IoP.
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * Sets the operating mode of the IoP.
	 * 
	 * @param mode The operating mode of the IoP.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * Gets the flag for connecting to remote peers or not.
	 * 
	 * @return The flag.
	 */
	public boolean isRemotes() {
		return remotes;
	}

	/**
	 * Sets the flag for connecting to remote peers or not.
	 * 
	 * @param remotes The flag.
	 */
	public void setRemotes(boolean remotes) {
		this.remotes = remotes;
	}

	/**
	 * Gets the number of unchoking slots per swarm for the IoP.
	 * 
	 * @return The number of unchoking slots.
	 */
	public int getSlots() {
		return slots;
	}

	/**
	 * Sets the number of unchoking slots per swarm for the IoP.
	 * 
	 * @param slots The number of unchoking slots.
	 */
	public void setSlots(int slots) {
		this.slots = slots;
	}

	/**
	 * Gets the time period T for the IoP to ask the SIS for torrent statistics.
	 * 
	 * @return The time period T.
	 */
	public int getT() {
		return t;
	}

	/**
	 * Sets the time period T for the IoP to ask the SIS for torrent statistics.
	 * 
	 * @param t The time period T.
	 */
	public void setT(int t) {
		this.t = t;
	}

	/**
	 * Gets the lower bound for upload bandwidth.
	 * 
	 * @return The lower bound for upload bandwidth.
	 */
	public float getUlow() {
		return ulow;
	}

	/**
	 * Sets the lower bound for upload bandwidth.
	 * 
	 * @param ulow The lower bound for upload bandwidth.
	 */
	public void setUlow(float ulow) {
		this.ulow = ulow;
	}

	/**
	 * Gets the lower bound for download bandwidth.
	 * 
	 * @return The lower bound for download bandwidth.
	 */
	public float getDlow() {
		return dlow;
	}

	/**
	 * Sets the lower bound for download bandwidth.
	 * 
	 * @param dlow The lower bound for download bandwidth.
	 */
	public void setDlow(float dlow) {
		this.dlow = dlow;
	}

	/**
	 * Gets the total upload bandwidth.
	 * 
	 * @return The total upload bandwidth.
	 */
	public float getU() {
		return u;
	}

	/**
	 * Sets the total upload bandwidth.
	 * 
	 * @param u The total upload bandwidth.
	 */
	public void setU(float u) {
		this.u = u;
	}

	/**
	 * Gets the total download bandwidth.
	 * 
	 * @return The total download bandwidth.
	 */
	public float getD() {
		return d;
	}

	/**
	 * Sets the total download bandwidth.
	 * 
	 * @param d The total download bandwidth.
	 */
	public void setD(float d) {
		this.d = d;
	}

	/**
	 * Gets the percentage of old swarms the IoP will keep participating in.
	 * 
	 * @return The percentage of old swarms.
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Sets the percentage of old swarms the IoP will keep participating in.
	 * 
	 * @param x The percentage of old swarms.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Gets the list of local IP ranges.
	 * 
	 * @return The list of local IP ranges.
	 */
	public String[] getLocalIPRanges() {
		return localIPRanges;
	}

	/**
	 * Sets the list of local IP ranges.
	 * 
	 * @param localIPRanges The list of local IP ranges.
	 */
	public void setLocalIPRanges(String[] localIPRanges) {
		this.localIPRanges = localIPRanges;
	}
	
	

}
