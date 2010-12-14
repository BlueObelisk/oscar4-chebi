package uk.ac.cam.ch.wwmm.oscar.bjoc.io;

import java.io.ByteArrayOutputStream;

import junit.framework.Assert;
import nu.xom.Builder;
import nu.xom.Document;

import org.junit.Test;

public class RolesOutputStreamTest {

	@Test
	public void testWellformedness() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RolesOutputStream chemOut = new RolesOutputStream(out);
		chemOut.close();
		String results = new String(out.toByteArray());
		Builder parser = new Builder();
		Document doc = parser.build(results, "http://www.example.org/");
		Assert.assertNotNull(doc);
	}

}