package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

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

		String line = reader.readLine();
		int counter = 0;
		while (line != null && counter < 15) {
			counter++;
			String pmcid = line.trim();
			ProcessPaper paperProcessor = new ProcessPaper(pmcid);
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
