package uk.ac.cam.ch.wwmm.oscar.bjoc.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.NamedEntityWithRoles;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.Role;
import uk.ac.cam.ch.wwmm.oscar.bjoc.RecoveredChemistry;

public class RolesOutputStream {

	private PrintStream out;

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
		out.println("      xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
		out.println("      xmlns:obo=\"http://purl.obolibrary.org/obo#\"");
		out.println("      xmlns:cheminf=\"http://semanticscience.org/resource/\" xml:lang=\"en\">");
		out.println("<head>");
		out.println("  <title>Results</title>");
		out.println("  <link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" media=\"print, projection, screen\" />");
		out.println("  <script type=\"text/javascript\" src=\"./jquery-latest.js\"></script>");
		out.println("  <script type=\"text/javascript\" src=\"./jquery.tablesorter.js\"></script>");
		out.println("</head>");
		out.println("<body>");

		StringBuilder builder = new StringBuilder();
		builder.append("<table id=\"table0"
				+ "\" class=\"tablesorter\">").append("\n");
		builder.append("<thead>").append("\n");
		builder.append("<tr><th>Compound</th><th>Role</th><th>Paper</th><th>Sentence</th></tr>").append("\n");
		builder.append("</thead>").append("\n");
		builder.append("<tbody rel=\"oscar:lists\">").append("\n");
		out.println(builder.toString());
	}

	public void write(RecoveredChemistry chemistry) {
		String pmcid = chemistry.getPmcid();
		
		// output chemical entities
		StringBuilder builder = new StringBuilder();
		Collection<NamedEntityWithRoles> roles = chemistry.getRoles();
		int roleCount = 0;
		for (NamedEntityWithRoles compound : roles) {
			List<Role> roleList = compound.getRoles();
			for (Role chemicalRole : roleList) {
				if (!("None".equals(chemicalRole.getRole()))) {
					roleCount++;
					builder.append(
						" <tr resource=\"#mol" + pmcid + "_" + roleCount + "\">"
					).append("\n");
					builder.append(
						"<td><span property=\"rdfs:label\">" + compound.getNamedEntity() + "</span>" +
						"  <a rel=\"rdfs:subClassOf\" href=\"http://semanticscience.org/resource/CHEMINF_000000\" />" +
						"</td>").append("\n"
					);
					builder.append("<td>" + wrapRole(chemicalRole.getRole()) + "</td>").append("\n");
					builder.append("<td rel=\"oscar:foundIn\">" +
							"<a resource=\"#"+ pmcid + "\" typeof=\"bibo:Article\" rel=\"foaf:homepage\" "
							+ "property=\"dc:identifier\" "
							+ "href=\"http://www.ncbi.nlm.nih.gov/sites/ppmc/articles/"
							+ "PMC" + pmcid + "\">PCM"
							+ pmcid + "</a></td>");
					builder.append("<td>" + chemicalRole.getSentence() + "</td>").append("\n");
					builder.append("</tr>").append("\n");
				}
			}
		}
		if (roleCount > 0) {
			// only print tables with content
			out.println(builder.toString());
		}

		out.flush();
	}

	private String wrapRole(String role) {
		if ("Solvent".equals(role)) {
			role = "<div rel=\"obo:has_role\"><span resource=\"chebi:46787\">" + role + "</span></div>";
		}
		return role;
	}

	public void close() throws IOException {
		out.println("</tbody>");
		out.println("</table>");
		out.println("  <script type=\"text/javascript\" id=\"js\">"
				+ "   $(document).ready(function() {"
				+ "	// call the tablesorter plugin");
		out.println("	$(\"#table0\").tablesorter();");
		out.println("}); </script>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}
}
