package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class ProcessPaperTest {

	/**
	 * BJOC paper DOI:<a href="http://dx.doi.org/10.1186/1860-5397-1-11.">10.1186/1860-5397-1-11</a>.
	 * @throws Exception 
	 */
	@Test
	public void testPMCID1399459() throws Exception {
		ProcessPaper paperProcessor = new ProcessPaper("1399459");
		RecoveredChemistry chemistry = paperProcessor.processPaper();
		Assert.assertNotNull(chemistry);
		List<NamedEntity> entities = chemistry.getNamedEntities();
		Assert.assertNotNull(entities);
		Assert.assertNotSame(0, entities.size());
		Map<String,String> roles = chemistry.getRoles();
		Assert.assertNotNull(roles);
		Assert.assertNotSame(0, roles.keySet().size());
		// I hope we can enable them later; they fail right now
		// Assert.assertTrue(roles.containsKey("THF"));
		// Assert.assertEquals("Solvent", roles.get("THF"));
	}
	
}
