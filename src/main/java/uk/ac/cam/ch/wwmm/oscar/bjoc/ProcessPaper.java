package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.htmlparser.jericho.Source;
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

		Document doc = builder.build(
			this.getClass().getClassLoader().getResourceAsStream(
				file
			)
		);
		
		Nodes nodes = doc.query("//div[@class='section-content']");
		for (int i=0; i<nodes.size(); i++) {
			Node node = nodes.get(i);
			String text = node.getValue();
			processText(chemistry, text);
		}

		return chemistry;
	}

	private void processText(RecoveredChemistry chemistry, String text) {
		Source source = new Source(text);
		text = source.getTextExtractor().toString();

		try {
			List<NamedEntity> entities = oscar.getNamedEntities(text);
			chemistry.addNamedEntities(entities);
			chemistry.addResolvedNamedEntities(oscar.resolveNamedEntities(entities));
		} catch (Exception e) {
			// ignore for now
		}
		
		Document parsedDoc = docCreator.runChemicalTagger(text);
		chemistry.addRoles(roleIdentifier.getRoles(parsedDoc));
		chemistry.setSentenceCount(ParsedDocumentStatistics.getSentenceCount(parsedDoc));
		chemistry.setPrepPhraseCount(ParsedDocumentStatistics.getPrepPhraseCount(parsedDoc));
		chemistry.setDissolvePhraseCount(ParsedDocumentStatistics.getDissolvePhraseCount(parsedDoc));
	}

	public String streamAsString(InputStream stream) throws IOException {
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
	
}
