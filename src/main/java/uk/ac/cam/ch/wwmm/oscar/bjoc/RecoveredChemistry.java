package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.NamedEntityWithRoles;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class RecoveredChemistry {

	private String pmcid;
	private Collection<NamedEntityWithRoles> roles;
	private List<NamedEntity> entities;
	private Map<NamedEntity, String> resolvedEntities;
	private int sentenceCount;
	private int prepPhraseCount;
	private int dissolvePhraseCount;

	public RecoveredChemistry(String pmcid) {
		this.pmcid = pmcid;
		this.sentenceCount = 0;
		this.prepPhraseCount = 0;
		this.dissolvePhraseCount = 0;
	}

	public void setNamedEntities(List<NamedEntity> entities) {
		this.entities = entities;
	}

	public void setResolvedNamedEntities(Map<NamedEntity,String> resolvedEntities) {
		this.resolvedEntities = resolvedEntities;
	}

	public void setRoles(Collection<NamedEntityWithRoles> roles) {
		this.roles = roles;
	}

	public Collection<NamedEntityWithRoles> getRoles() {
		return Collections.unmodifiableCollection(this.roles);
	}

	public List<NamedEntity> getNamedEntities() {
		return Collections.unmodifiableList(entities);
	}

	public Map<NamedEntity,String> getResolvedNamedEntities() {
		return Collections.unmodifiableMap(resolvedEntities);
	}

	public String getPmcid() {
		return pmcid;
	}

	public void setSentenceCount(int sentenceCount) {
		this.sentenceCount = sentenceCount;
	}

	public int getSentenceCount() {
		return sentenceCount;
	}

	public void setPrepPhraseCount(int prepPhraseCount) {
		this.prepPhraseCount = prepPhraseCount;
	}

	public int getPrepPhraseCount() {
		return prepPhraseCount;
	}

	public void setDissolvePhraseCount(int dissolvePhraseCount) {
		this.dissolvePhraseCount = dissolvePhraseCount;
	}

	public int getDissolvePhraseCount() {
		return dissolvePhraseCount;
	}
}
