package uk.ac.cam.ch.wwmm.oscar.bjoc.io;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import junit.framework.Assert;
import nu.xom.Builder;
import nu.xom.Document;

import org.junit.Test;

public class RecoveredChemistryOutputStreamTest {

	@Test
	public void testWellformedness() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RecoveredChemistryOutputStream chemOut =
			new RecoveredChemistryOutputStream(out);
		chemOut.close();
		String results = new String(out.toByteArray());
		Builder parser = new Builder();
		Document doc = parser.build(results, "http://www.example.org/");
		Assert.assertNotNull(doc);
	}

}
