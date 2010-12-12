package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class RolesOutputStream {

	private PrintStream out;

	private int tableCounter = 0;

	public RolesOutputStream(OutputStream stream) {
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
		out.println("<div><p resource=\"#" + chemistry.getPmcid()
				+ "\" typeof=\"bibo:Article\">Paper: <a rel=\"foaf:homepage\" "
				+ "property=\"dc:identifier\" "
				+ "href=\"http://www.ncbi.nlm.nih.gov/sites/ppmc/articles/"
				+ "PMC" + chemistry.getPmcid() + "\">PCM"
				+ chemistry.getPmcid() + "</a></p>");

		// output chemical entities
		tableCounter++;
		StringBuilder builder = new StringBuilder();
		builder.append("<table rel=\"oscar:lists\" id=\"table" + tableCounter
				+ "\" class=\"tablesorter\">").append("\n");
		builder.append("<thead>").append("\n");
		builder.append("<tr><th>Compound</th><th>Role</th></tr>").append("\n");
		builder.append("</thead>").append("\n");
		builder.append("<tbody>").append("\n");
		Map<String, List<String>> roles = chemistry.getRoles();
		int roleCount = 0;
		for (String compound : roles.keySet()) {
			List<String> roleList = roles.get(compound);
			for (String chemicalRole : roleList) {
				if (!("None".equals(chemicalRole))) {
					roleCount++;
					builder.append("<tr>").append("\n");
					builder.append("<td>" + compound.trim() + "</td>").append("\n");
					builder.append("<td>" + chemicalRole + "</td>").append("\n");
					builder.append("</tr>").append("\n");
				}
			}
		}
		builder.append("</tbody>").append("\n");
		builder.append("</table>").append("\n");
		if (roleCount > 0) {
			// only print tables with content
			out.println(builder.toString());
		}

		// output roles
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
}
