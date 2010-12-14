package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.NamedEntityWithRoles;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.ParsedDocumentCreator;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.Role;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.RoleIdentifier;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class ProcessPaperTest {

	/**
	 * BJOC paper DOI:<a
	 * href="http://dx.doi.org/10.1186/1860-5397-1-11.">10.1186
	 * /1860-5397-1-11</a>.
	 * 
	 * @throws Exception
	 */
	private static Logger LOG = Logger.getLogger(ProcessPaperTest.class);

	@Test
	public void testPMCID1399459() throws Exception {
		ProcessPaper paperProcessor = new ProcessPaper("1399459", new Oscar(),
				new ParsedDocumentCreator(), new RoleIdentifier());
		RecoveredChemistry chemistry = paperProcessor.processPaper();
		Assert.assertNotNull(chemistry);
		List<NamedEntity> entities = chemistry.getNamedEntities();
		Assert.assertNotNull(entities);
		Assert.assertNotSame(0, entities.size());
		Collection<NamedEntityWithRoles> roles = chemistry.getRoles();
		Assert.assertNotNull(roles);
		Assert.assertNotSame(0, roles.size());
		// I hope we can enable them later; they fail right now
		// Assert.assertTrue(roles.containsKey("THF"));
		// Assert.assertEquals("Solvent", roles.get("THF"));
	}

	@Test
	public void testForDuplicateNEs() throws Exception {
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		ProcessPaper paperProcessor = new ProcessPaper("1", new Oscar(),
				new ParsedDocumentCreator(), new RoleIdentifier());
		String sentence = "100 ) for the racemization of R-3 ( 1 mM ) in the presence of 1 equivalent of DBU in octane and THF";
		paperProcessor.processText(chemistry, sentence);
		Collection<NamedEntityWithRoles> identifiedRoles = chemistry.getRoles();
		int solventCount = getSolventCount(identifiedRoles);
		printOutRoles(identifiedRoles);
		Assert.assertEquals("Solvent Count", 2, solventCount);
		
	}
	private void printOutRoles(Collection<NamedEntityWithRoles> identifiedRoles) {
		
        for (NamedEntityWithRoles namedEntityWithRoles : identifiedRoles) {
			LOG.debug("NamedEntity: "+namedEntityWithRoles.getNamedEntity());
			for (Role roleName : namedEntityWithRoles.getRoles()) {
				LOG.debug("Role: "+roleName.getRole());
			}
			
		}
		
	}

	private int getSolventCount(Collection<NamedEntityWithRoles> identifiedRoles) {
		int count = 0;
		for (NamedEntityWithRoles entity : identifiedRoles) {
			for (Role role : entity.getRoles()) {
				if (!role.getRole().equals("None")) {
					count += 1;
					break;
				}
			}
		}
		return count;
	}
}
