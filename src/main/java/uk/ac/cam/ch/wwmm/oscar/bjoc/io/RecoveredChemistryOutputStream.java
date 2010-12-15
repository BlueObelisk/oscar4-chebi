package uk.ac.cam.ch.wwmm.oscar.bjoc.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.cam.ch.wwmm.oscar.bjoc.RecoveredChemistry;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class RecoveredChemistryOutputStream {

	private PrintStream out;

	private int tableCounter = 0;

	public RecoveredChemistryOutputStream(OutputStream stream) {
		if (out instanceof PrintStream) {
			this.out = (PrintStream) stream;
		} else {
			this.out = new PrintStream(stream);
		}
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\""
				+ " \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" version=\"XHTML+RDFa 1.0\"");
		out.println("      xmlns:bibo=\"http://purl.org/ontology/bibo/\"");
		out.println("      xmlns:oscar=\"http://oscar3-chem.sf.net/ontology/\"");
		out.println("      xmlns:dc=\"http://purl.org/dc/terms/\"");
		out.println("      xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"");
		out.println("      xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
		out.println("      xmlns:cheminf=\"http://semanticscience.org/resource/\" xml:lang=\"en\">");
		out.println("<head>");
		out.println("  <title>Results</title>");
		out.println("  <link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" media=\"print, projection, screen\" />");
		out.println("  <script type=\"text/javascript\" src=\"./jquery-latest.js\"></script>");
		out.println("  <script type=\"text/javascript\" src=\"./jquery.tablesorter.js\"></script>");
		out.println("</head>");
		out.println("<body>");

		tableCounter = 0;
	}

	public void write(RecoveredChemistry chemistry) {
		String pmcid = chemistry.getPmcid();
		out.println("<div><p resource=\"#" + pmcid
				+ "\" typeof=\"bibo:Article\">Paper: <a rel=\"foaf:homepage\" "
				+ "property=\"dc:identifier\" "
				+ "href=\"http://www.ncbi.nlm.nih.gov/sites/ppmc/articles/"
				+ "PMC" + pmcid + "\">PCM"
				+ pmcid + "</a></p>");

		// output chemical entities
		tableCounter++;
		StringBuilder builder = new StringBuilder();
		builder.append("<table resource=\"#" + pmcid
				+ "\" id=\"table" + tableCounter
				+ "\" class=\"tablesorter\">").append("\n");
		builder.append("<thead>").append("\n");
		builder.append("<tr><th>Compound</th><th>Confidence</th><th>InChI</th></tr>").append("\n");
		builder.append("</thead>").append("\n");
		builder.append("<tbody rel=\"oscar:lists\">").append("\n");
		List<String> alreadyDone = new ArrayList<String>();
		Map<NamedEntity, String> resolvedEntities = chemistry
				.getResolvedNamedEntities();
		int entityCount = 0;
		int structureCount = 0;
		for (NamedEntity entity : chemistry.getNamedEntities()) {
			if (!alreadyDone.contains(entity.getSurface())) {
				if (resolvedEntities.containsKey(entity)
						|| entity.getConfidence() > 0.5) {
					builder.append(
						" <tr resource=\"#mol" + pmcid + "_" + entityCount + "\">"
					).append("\n");
					builder.append(
						"  <td><span property=\"rdfs:label\">" + entity.getSurface() + "</span>" +
						"  <a rel=\"rdfs:subClassOf\" href=\"http://semanticscience.org/resource/CHEMINF_000000\" />" +
						"</td>").append("\n");
					builder.append("  <td>" + round(entity.getConfidence())
							+ "</td>").append("\n");
					if (resolvedEntities.containsKey(entity)) {
						builder.append(
							"  <td rel=\"cheminf:CHEMINF_000200\">" +
							"<span resource=\"#mol" + pmcid + "_" + entityCount + "_inchi\"" +
							"      typeof=\"cheminf:CHEMINF_000113\"" +
							"      property=\"cheminf:SIO_000300\">" +
							resolvedEntities.get(entity)
							+ "</span></td>").append("\n");
						structureCount++;
					} else {
						builder.append(" <td />").append("\n");
					}
					alreadyDone.add(entity.getSurface());
					entityCount++;
					builder.append(" </tr>").append("\n");
				}
			}
		}
		builder.append("</tbody>").append("\n");
		builder.append("</table>").append("\n");
		if (entityCount > 0) {
			// only print tables with content
			out.println(builder.toString());
		}

		out.println("</div>");
		out.flush();
	}

	public void close() throws IOException {
		out.println("  <script type=\"text/javascript\" id=\"js\">"
				+ "   $(document).ready(function() {"
				+ "	// call the tablesorter plugin");
		for (int i = 1; i <= tableCounter; i++) {
			out.println("	$(\"#table" + i + "\").tablesorter();");
		}
		out.println("}); </script>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	private static double round(double confidence) {
		return (Math.round(confidence * 100.0)) / 100.0;
	}
}
