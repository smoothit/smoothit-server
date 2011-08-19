package eu.smoothit.sis.metering;

import javax.ejb.Local;

@Local
public interface MeteringTimer {
	public void startTimer();
	public void stopTimer();
}
