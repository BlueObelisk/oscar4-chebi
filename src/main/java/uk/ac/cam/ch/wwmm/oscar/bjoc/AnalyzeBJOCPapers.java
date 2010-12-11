package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import uk.ac.cam.ch.wwmm.chemicaltagger.RoleIdentifier;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

public class AnalyzeBJOCPapers {

	public static void main(String[] args) throws Exception {
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
		RoleIdentifier roleIdentifier = new RoleIdentifier();
		String line = reader.readLine();
		int counter = 0;
		while (line != null) {
			counter++;
			System.out.println("Paper: " + counter);
			String pmcid = line.trim();
			ProcessPaper paperProcessor = new ProcessPaper(pmcid, oscar, roleIdentifier);
			RecoveredChemistry chemistry = paperProcessor.processPaper();
			out.write(chemistry);
			roles.write(chemistry);
			line = reader.readLine();
		};
		reader.close();
		roles.close();
		out.close();
	}
	
}
