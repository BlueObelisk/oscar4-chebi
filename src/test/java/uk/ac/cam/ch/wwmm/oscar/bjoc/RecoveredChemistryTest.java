package uk.ac.cam.ch.wwmm.oscar.bjoc;

import org.junit.Assert;
import org.junit.Test;

public class RecoveredChemistryTest {

	@Test
	public void testGetSetPMCID() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertEquals("1", chemistry.getPmcid());
	}

	@Test
	public void testGetSetDissolvePhraseCount() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertEquals(0, chemistry.getDissolvePhraseCount()); // the default
		chemistry.setDissolvePhraseCount(5);
		Assert.assertEquals(5, chemistry.getDissolvePhraseCount());
	}

	@Test
	public void testGetSetSentenceCount() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertEquals(0, chemistry.getSentenceCount()); // the default
		chemistry.setSentenceCount(5);
		Assert.assertEquals(5, chemistry.getSentenceCount());
	}

	@Test
	public void testGetSetPrepPhraseCount() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertEquals(0, chemistry.getPrepPhraseCount()); // the default
		chemistry.setPrepPhraseCount(5);
		Assert.assertEquals(5, chemistry.getPrepPhraseCount());
	}

}
