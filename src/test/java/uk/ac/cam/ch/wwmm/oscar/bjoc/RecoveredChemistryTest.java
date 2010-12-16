package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.NamedEntityWithRoles;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.Role;

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

	@Test
	public void testGetNamedEntities() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertNotNull(chemistry.getNamedEntities());
	}

	@Test
	public void testGetRoles() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertNotNull(chemistry.getRoles());
	}

	@SuppressWarnings("serial")
	@Test
	public void testAddRoles() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertNotNull(chemistry.getRoles());
		Assert.assertEquals(0, chemistry.getRoles().size());
		chemistry.addRoles(new ArrayList<NamedEntityWithRoles>() {{
			NamedEntityWithRoles entity = new NamedEntityWithRoles("benzene");
			entity.addRole(new Role("Solvent", "Benzene is a solvent."));
			add(entity);
		}});
		Assert.assertEquals(1, chemistry.getRoles().size());
	}

	@Test
	public void testGetResolvedNamedEntities() {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		Assert.assertNotNull(chemistry.getResolvedNamedEntities());
	}
	
	
}
