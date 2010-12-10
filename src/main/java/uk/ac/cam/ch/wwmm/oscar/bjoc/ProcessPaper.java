package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Source;
import uk.ac.cam.ch.wwmm.chemicaltagger.RoleIdentifier;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

public class ProcessPaper {

	public static String streamAsString(InputStream stream) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(stream)
		);
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		stream.close();
		return builder.toString();
	}
	
	public static void main(String[] args) throws Exception {
		Oscar oscar = new Oscar();
		oscar.getDictionaryRegistry().register(new OpsinDictionary());

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
			RecoveredChemistry chemistry = new RecoveredChemistry(pmcid);
			chemistries.add(chemistry);
			String file = "bjoc/" + pmcid + ".html";
			
			String text = streamAsString(
				ProcessPaper.class.getClassLoader().getResourceAsStream(
					file
				)
			);

			Source source = new Source(text);
			text = source.getTextExtractor().toString();

			try {
				List<NamedEntity> entities = oscar.getNamedEntities(text);
				chemistry.setNamedEntities(entities);
				chemistry.setResolvedNamedEntities(oscar.resolveNamedEntities(entities));
			} catch (Exception e) {
				// ignore for now
			}
			
			RoleIdentifier roleIdentifier = new RoleIdentifier(text);
			chemistry.setRoles(roleIdentifier.getRoles());
			line = reader.readLine();
		};
		reader.close();

		PrintStream out = new PrintStream(
			new FileOutputStream("/home/egonw/bjoc.txt")
		);
		for (RecoveredChemistry chemistry : chemistries) {
			out.println("file: " + chemistry.getPmcid());

			// output chemical entities
			List<String> alreadyDone = new ArrayList<String>();
			Map<NamedEntity,String> resolvedEntities = chemistry.getResolvedNamedEntities();
			int entityCount = 0;
			int structureCount = 0;
			for (NamedEntity entity : chemistry.getNamedEntities()) {
				if (!alreadyDone.contains(entity.getSurface())) {
					if (resolvedEntities.containsKey(entity)) {
						out.print("  " + entity.getSurface() + ", " + 
							round(entity.getConfidence()));
						out.println(" (" + resolvedEntities.get(entity) + ")");
						alreadyDone.add(entity.getSurface());
						entityCount++;
						structureCount++;
					} else if (entity.getConfidence() > 0.5) {
						out.print("  " + entity.getSurface() + ", " + 
								round(entity.getConfidence()));
						out.println();
						alreadyDone.add(entity.getSurface());
						entityCount++;
					}
				}
			}

			// output roles
			Map<String,String> roles = chemistry.getRoles();
			for (String compound : roles.keySet()) {
				String role = roles.get(compound).trim();
				if (!("None".equals(role))) {
					out.println(compound.trim() + ": " + role);
				}
			}
		}
	}

	private static double round(double confidence) {
		return (Math.round(confidence*100.0))/100.0;
	}
	
}
