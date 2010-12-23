package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.ParsedDocumentCreator;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.RoleIdentifier;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.bjoc.io.RecoveredChemistryOutputStream;
import uk.ac.cam.ch.wwmm.oscar.bjoc.io.RolesOutputStream;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

public class AnalyzeSomeBJOCPapersTest {

	@SuppressWarnings("serial")
	@Test
	public void testSomePapers() throws Exception {
		List<String> whitelist = new ArrayList<String>() {{
			add("1399459");
			add("2395297");
			add("2605621");
			add("2887305");
			add("2956567");
		}};

		File outputDir = new File("target", "output");
		if (!outputDir.exists()) outputDir.mkdir();
		RecoveredChemistryOutputStream out = new RecoveredChemistryOutputStream(
			new FileOutputStream(
				new File(outputDir, "bjoc.html")
			)
		);
		RolesOutputStream roles = new RolesOutputStream(
			new FileOutputStream(
				new File(outputDir, "roles.html")
			)
		);

		Oscar oscar = new Oscar();
		oscar.getDictionaryRegistry().register(new OpsinDictionary());
		ParsedDocumentCreator docCreator = new ParsedDocumentCreator();
		RoleIdentifier roleIdentifier = new RoleIdentifier();
		for (String pmcid : whitelist) {
			ProcessPaper paperProcessor = new ProcessPaper(
					pmcid, oscar, docCreator, roleIdentifier
			);
			RecoveredChemistry chemistry = paperProcessor.processPaper();
			out.write(chemistry);
			roles.write(chemistry);
		};
		String validButton =
			"<p about=\"\" resource=\"http://www.w3.org/TR/rdfa-syntax\"" +
			"     rel=\"dc:conformsTo\" xmlns:dc=\"http://purl.org/dc/terms/\">" +
			"    <a href=\"http://validator.w3.org/check?uri=https%3A%2F%2Fhudson.ch.cam.ac.uk%2Fjob%2Foscar4-chebi%2Fws%2Ftarget%2Foutput%2Fbjoc.html&amp;charset=%28detect+automatically%29&amp;doctype=Inline&amp;group=0\"><img" +
			"        src=\"http://www.w3.org/Icons/valid-xhtml-rdfa\"" +
			"        alt=\"Valid XHTML + RDFa\" height=\"31\" width=\"88\" /></a>" +
			"  </p>";
		roles.close(validButton.replace("bjoc.html", "roles.html"));
		out.close(validButton);
	}
	
}
