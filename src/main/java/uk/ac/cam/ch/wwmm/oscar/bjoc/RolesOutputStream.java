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
		out.println("<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" media=\"print, projection, screen\" />");
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
		out.println("<table rel=\"oscar:lists\" id=\"table" + tableCounter
				+ "\" class=\"tablesorter\">");
		out.println("<thead>");
		out.println("<tr><th>Compound</th><th>Role</th></tr>");
		out.println("</thead>");
		out.println("<tbody>");
		Map<String, List<String>> roles = chemistry.getRoles();
		for (String compound : roles.keySet()) {
			/*****************
			 * @lh359
			 */
			List<String> roleList = roles.get(compound);
			for (String chemicalRole : roleList) {

				if (!("None".equals(chemicalRole))) {
					out.println("<tr>");
					out.println("<td>" + compound.trim() + "</td>");
					out.println("<td>" + chemicalRole + "</td>");
					out.println("</tr>");
				}
			}
		}
		out.println("</tbody>");
		out.println("</table>");

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
