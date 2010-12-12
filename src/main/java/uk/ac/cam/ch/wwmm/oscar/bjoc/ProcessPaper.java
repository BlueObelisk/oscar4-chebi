package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.htmlparser.jericho.Source;
import nu.xom.Document;
import uk.ac.cam.ch.wwmm.chemicaltagger.ParsedDocumentCreator;
import uk.ac.cam.ch.wwmm.chemicaltagger.ParsedDocumentStatistics;
import uk.ac.cam.ch.wwmm.chemicaltagger.RoleIdentifier;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class ProcessPaper {

	private Oscar oscar;
	ParsedDocumentCreator docCreator;
	private RoleIdentifier roleIdentifier;
	private String pmcid;
	
	public ProcessPaper(String pmcid, Oscar oscar,
		ParsedDocumentCreator docCreator, RoleIdentifier roleIdentifier)
	throws Exception {
		this.pmcid = pmcid;
		this.oscar = oscar;
		this.docCreator = docCreator;
		this.roleIdentifier = roleIdentifier;
	}

	public RecoveredChemistry processPaper() throws IOException {
		RecoveredChemistry chemistry = new RecoveredChemistry(pmcid);
		String file = "bjoc/" + pmcid + ".html";
		
		String text = streamAsString(
			this.getClass().getClassLoader().getResourceAsStream(
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
		
		Document parsedDoc = docCreator.runChemicalTagger(text);
		chemistry.setRoles(roleIdentifier.getRoles(parsedDoc));
		chemistry.setSentenceCount(ParsedDocumentStatistics.getSentenceCount(parsedDoc));
		chemistry.setPrepPhraseCount(ParsedDocumentStatistics.getPrepPhraseCount(parsedDoc));
		chemistry.setDissolvePhraseCount(ParsedDocumentStatistics.getDissolvePhraseCount(parsedDoc));

		return chemistry;
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
