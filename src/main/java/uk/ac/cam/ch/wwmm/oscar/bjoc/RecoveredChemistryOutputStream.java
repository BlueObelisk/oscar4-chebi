package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class RecoveredChemistryOutputStream {

	private PrintStream out;

	public RecoveredChemistryOutputStream(OutputStream stream) {
		if (out instanceof PrintStream) {
			this.out = (PrintStream)stream;
		} else {
			this.out = new PrintStream(stream);
		}
	}

	public void write(RecoveredChemistry chemistry) {
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

	public void close() throws IOException {
		out.close();
	}

	private static double round(double confidence) {
		return (Math.round(confidence*100.0))/100.0;
	}
}
