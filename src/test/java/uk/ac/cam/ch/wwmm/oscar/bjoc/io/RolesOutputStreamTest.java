package uk.ac.cam.ch.wwmm.oscar.bjoc.io;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import nu.xom.Builder;
import nu.xom.Document;

import org.junit.Test;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.NamedEntityWithRoles;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.Role;
import uk.ac.cam.ch.wwmm.oscar.bjoc.RecoveredChemistry;

public class RolesOutputStreamTest {

	@Test
	public void testWellformedness_Wrapper() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RolesOutputStream chemOut = new RolesOutputStream(out);
		chemOut.close();
		String results = new String(out.toByteArray());
		Builder parser = new Builder();
		Document doc = parser.build(results, "http://www.example.org/");
		Assert.assertNotNull(doc);
	}

	@Test
	public void testWellformedness_Crash() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RolesOutputStream chemOut = new RolesOutputStream(out);
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		chemistry.addCrash(
			"This sentence caused a crash.",
			new Exception("Something went wrong.")
		);
		chemOut.write(chemistry);
		chemOut.close();
		String results = new String(out.toByteArray());
		Builder parser = new Builder();
		Document doc = parser.build(results, "http://www.example.org/");
		Assert.assertNotNull(doc);
	}

	@Test
	public void testWellformedness_NamedEntitiesWithRoles() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RolesOutputStream chemOut = new RolesOutputStream(out);
		RecoveredChemistry chemistry = new RecoveredChemistry("1");
		List<NamedEntityWithRoles> roles = new ArrayList<NamedEntityWithRoles>();
		NamedEntityWithRoles entity = new NamedEntityWithRoles("benzene");
		entity.addRole(new Role("Solvent", "Benzene was used as solvent."));
		roles.add(entity);
		chemistry.addRoles(
			roles
		);
		chemOut.write(chemistry);
		chemOut.close();
		String results = new String(out.toByteArray());
		Assert.assertTrue(results.contains("benzene"));
		Builder parser = new Builder();
		Document doc = parser.build(results, "http://www.example.org/");
		Assert.assertNotNull(doc);
	}

}
