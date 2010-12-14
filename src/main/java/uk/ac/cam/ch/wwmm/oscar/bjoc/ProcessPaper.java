package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.ParsedDocumentCreator;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.ParsedDocumentStatistics;
import uk.ac.cam.ch.wwmm.chemicaltagger.roles.RoleIdentifier;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class ProcessPaper {

	private Oscar oscar;
	ParsedDocumentCreator docCreator;
	private RoleIdentifier roleIdentifier;
	private String pmcid;
	private Builder builder;

	
	public ProcessPaper(String pmcid, Oscar oscar,
		ParsedDocumentCreator docCreator, RoleIdentifier roleIdentifier)
	throws Exception {
		this.pmcid = pmcid;
		this.oscar = oscar;
		this.docCreator = docCreator;
		this.roleIdentifier = roleIdentifier;
		this.builder = new Builder();
	}

	public RecoveredChemistry processPaper() throws Exception {
		RecoveredChemistry chemistry = new RecoveredChemistry(pmcid);
		String file = "bjoc/" + pmcid + ".html";

		String text = streamAsString(
			this.getClass().getClassLoader().getResourceAsStream(
				file
			)
		);
//		System.out.println(text);
		Document doc = builder.build(
			new StringReader(text)
		);
		
		Nodes nodes = doc.query("//*[local-name()='div']/.[@class='section-content']");
		for (int i=0; i<nodes.size(); i++) {
			Node node = nodes.get(i);
			String textPart = node.getValue().trim();
			if (textPart.length() > 0)
				processText(chemistry, textPart);
		}

		return chemistry;
	}

	protected void processText(RecoveredChemistry chemistry, String text) {
		try {
			List<NamedEntity> entities = oscar.getNamedEntities(text);
			chemistry.addNamedEntities(entities);
			chemistry.addResolvedNamedEntities(oscar.resolveNamedEntities(entities));
		} catch (Exception e) {
			// ignore for now
		}

		try {
			chemistry.newSection();
			Document parsedDoc = docCreator.runChemicalTagger(text);
			chemistry.addRoles(roleIdentifier.getRoles(parsedDoc));
			chemistry.setSentenceCount(ParsedDocumentStatistics.getSentenceCount(parsedDoc));
			chemistry.setPrepPhraseCount(ParsedDocumentStatistics.getPrepPhraseCount(parsedDoc));
			chemistry.setDissolvePhraseCount(ParsedDocumentStatistics.getDissolvePhraseCount(parsedDoc));
		} catch (Throwable crash) {
			chemistry.addCrash(text, crash);
		}
	}

	public String streamAsString(InputStream stream) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(stream)
		);
		String line;
		while ((line = reader.readLine()) != null) {
			// remove the DOCTYPE line
			if (line.contains("DOCTYPE")) {
				while (!line.contains(">"))
					line = reader.readLine();
				line = reader.readLine();
			}
			builder.append(line).append("\n");
		}
		stream.close();
		return builder.toString();
	}
	
}
