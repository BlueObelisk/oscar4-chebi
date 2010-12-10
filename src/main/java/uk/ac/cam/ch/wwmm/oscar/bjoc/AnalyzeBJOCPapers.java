package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeBJOCPapers {

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				ProcessPaper.class.getClassLoader().getResourceAsStream(
					"pmcids.txt"
				)
			)
		);
		String line = reader.readLine();
		int counter = 0;
		List<RecoveredChemistry> chemistries = new ArrayList<RecoveredChemistry>();
		while (line != null && counter < 10) {
			counter++;
			String pmcid = line.trim();
			ProcessPaper paperProcessor = new ProcessPaper(pmcid);
			chemistries.add(paperProcessor.processPaper());
			line = reader.readLine();
		};
		reader.close();

		RecoveredChemistryOutputStream out = new RecoveredChemistryOutputStream(
			new FileOutputStream("/home/egonw/bjoc.txt")
		);
		for (RecoveredChemistry chemistry : chemistries) {
			out.write(chemistry);
		}
		out.close();
	}
	
}
