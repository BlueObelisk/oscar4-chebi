package uk.ac.cam.ch.wwmm.oscar.bjoc.io;

import java.io.ByteArrayOutputStream;

import junit.framework.Assert;
import nu.xom.Builder;
import nu.xom.Document;

import org.junit.Test;

import uk.ac.cam.ch.wwmm.oscar.bjoc.RecoveredChemistry;

public class RecoveredChemistryOutputStreamTest {

	@Test
	public void testWellformedness_Wrapper() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RecoveredChemistryOutputStream chemOut = new RecoveredChemistryOutputStream(out);
		chemOut.close();
		String results = new String(out.toByteArray());
		Builder parser = new Builder();
		Document doc = parser.build(results, "http://www.example.org/");
		Assert.assertNotNull(doc);
	}

	@Test
	public void testValidness_Wrapper() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RecoveredChemistryOutputStream chemOut = new RecoveredChemistryOutputStream(out);
		chemOut.close();
		String results = new String(out.toByteArray());
		Builder parser = new Builder();
		Document doc = parser.build(results, "http://www.example.org/");
		Assert.assertNotNull(doc);
	}

	@Test
	public void testWellformedness_Crash() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RecoveredChemistryOutputStream chemOut = new RecoveredChemistryOutputStream(out);
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
	public void testValidness_Crash() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RecoveredChemistryOutputStream chemOut = new RecoveredChemistryOutputStream(out);
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

}
