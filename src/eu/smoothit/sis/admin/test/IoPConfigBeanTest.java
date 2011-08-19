package eu.smoothit.sis.admin.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eu.smoothit.sis.admin.backendBean.IoPConfigBean;

public class IoPConfigBeanTest {
IoPConfigBean iop;
	@Before
	public void setUp() throws Exception {
		iop=new IoPConfigBean();
	}

	@Test
	public void testRetrieveInput() {
		String operationMode="operationMode";
		String remoteConnectionFlag="remoteConnectionFlag";
		String numberOfUnchokingSlots="numberOfUnchokingSlots";
		String timePeriodforStatistics="timePeriodforStatistics";
		String timePeriodforAge="timePeriodforAge";
		String timePeriodforOut="timePeriodforOut";
		String lowerBoundsForDownloadBandwidth="lowerBoundsForDownloadBandwidth";
		String lowerBoundsForUploadBandwidth="lowerBoundsForUploadBandwidth";
		String totalDownloadBandwidth="totalDownloadBandwidth";
		String totalUploadBandwidth="totalUploadBandwidth";
		String thePercentageValue="thePercentageValue";
		iop.setOperationMode(operationMode);
		iop.setRemoteConnectionFlag(remoteConnectionFlag);
		iop.setNumberOfUnchokingSlots(numberOfUnchokingSlots);
		iop.setTimePeriodforStatistics(timePeriodforStatistics);
		iop.setTimePeriodforAge(timePeriodforAge);
		iop.setTimePeriodforOut(timePeriodforOut);
		iop.setLowerBoundsForDownloadBandwidth(lowerBoundsForDownloadBandwidth);
		iop.setLowerBoundsForUploadBandwidth(lowerBoundsForUploadBandwidth);
		iop.setTotalDownloadBandwidth(totalDownloadBandwidth);
		iop.setTotalUploadBandwidth(totalUploadBandwidth);
		iop.setThePercentageValue(thePercentageValue);
		iop.retrieveInput();
		assertEquals(11,iop.getCachedProperties().size());
	}
}
