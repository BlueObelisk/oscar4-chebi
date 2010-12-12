package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.ch.wwmm.chemicaltagger.ParsedDocumentCreator;
import uk.ac.cam.ch.wwmm.chemicaltagger.RoleIdentifier;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

public class AnalyzeBJOCPapers {

	@SuppressWarnings("serial")
	public static void main(String[] args) throws Exception {
		List<String> blacklist = new ArrayList<String>() {{
			add("2874414");
		}};
		
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				ProcessPaper.class.getClassLoader().getResourceAsStream(
					"pmcids.txt"
				)
			)
		);
		RecoveredChemistryOutputStream out = new RecoveredChemistryOutputStream(
			new FileOutputStream("/home/egonw/bjoc.html")
		);
		RolesOutputStream roles = new RolesOutputStream(
			new FileOutputStream("/home/egonw/roles.html")
		);

		Oscar oscar = new Oscar();
		oscar.getDictionaryRegistry().register(new OpsinDictionary());
		ParsedDocumentCreator docCreator = new ParsedDocumentCreator();
		RoleIdentifier roleIdentifier = new RoleIdentifier();
		String line = reader.readLine();
		int counter = 0;
		while (line != null) {
			counter++;
			String pmcid = line.trim();
			System.out.println("Paper: " + counter + " (" + pmcid + ")");
			if (!blacklist.contains(pmcid)) {
				ProcessPaper paperProcessor = new ProcessPaper(
					pmcid, oscar, docCreator, roleIdentifier
				);
				RecoveredChemistry chemistry = paperProcessor.processPaper();
				out.write(chemistry);
				roles.write(chemistry);
			} else {
				System.out.println(" Blacklisted.");
			}
			line = reader.readLine();
		};
		reader.close();
		roles.close();
		out.close();
	}
	
}
