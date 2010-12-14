package uk.ac.cam.ch.wwmm.oscar.bjoc.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.NamedEntityWithRoles;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.Role;
import uk.ac.cam.ch.wwmm.oscar.bjoc.RecoveredChemistry;

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
		builder.append("<tr><th>Compound</th><th>Role</th><th>Sentence</th></tr>").append("\n");
		builder.append("</thead>").append("\n");
		builder.append("<tbody>").append("\n");
		Collection<NamedEntityWithRoles> roles = chemistry.getRoles();
		int roleCount = 0;
		for (NamedEntityWithRoles compound : roles) {
			List<Role> roleList = compound.getRoles();
			for (Role chemicalRole : roleList) {
				if (!("None".equals(chemicalRole.getRole()))) {
					roleCount++;
					builder.append("<tr>").append("\n");
					builder.append("<td>" + compound.getNamedEntity() + "</td>").append("\n");
					builder.append("<td>" + chemicalRole.getRole() + "</td>").append("\n");
					builder.append("<td>" + chemicalRole.getSentence() + "</td>").append("\n");
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

		// output sentence stats
		out.println("<p>");
		out.println("Number of sentences: " + chemistry.getSentenceCount() + "<br />");
		out.println("Number of preparation phrases: " + chemistry.getPrepPhraseCount() + "<br />");
		out.println("Number of dissolve phrases: " + chemistry.getDissolvePhraseCount() + "<br />");
		out.println("</p>");
		Map<String,Throwable> crashes = chemistry.getCrashes();
		for (String text : crashes.keySet()) {
			out.println("<p>");
			out.println("<b>" + crashes.get(text) + "</b><br />");
			out.println(text);
			out.println("</p>");
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
}
