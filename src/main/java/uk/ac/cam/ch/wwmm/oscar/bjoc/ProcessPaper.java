package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.htmlparser.jericho.Source;
import uk.ac.cam.ch.wwmm.chemicaltagger.RoleIdentifier;

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
		return builder.toString();
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				ProcessPaper.class.getClassLoader().getResourceAsStream(
					"pmcids.txt"
				)
			)
		);
		String line = reader.readLine();
		int counter = 0;
		Map<String,HashMap<String,String>> fileContents =
			new HashMap<String, HashMap<String,String>>();
		while (line != null && counter < 10) {
			counter++;
			String pmcid = line.trim();
			String file = "bjoc/" + pmcid + ".html";
			
			String text = streamAsString(
				ProcessPaper.class.getClassLoader().getResourceAsStream(
					file
				)
			);
			Source source = new Source(text);
			text = source.getTextExtractor().toString();

			RoleIdentifier roleIdentifier = new RoleIdentifier(text);
			HashMap<String,String> roles = roleIdentifier.getRoles();
			fileContents.put(pmcid, roles);
			line = reader.readLine();
		};

		for (String pmcid : fileContents.keySet()) {
			System.out.println("file: " + pmcid);
			HashMap<String,String> roles = fileContents.get(pmcid);
			for (String compound : roles.keySet()) {
				String role = roles.get(compound).trim();
				if (!("None".equals(role))) {
					System.out.println(compound.trim() + ": " + role);
				}
			}
		}
	}
	
}
