package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.cam.ch.wwmm.chemicaltagger.roles.NamedEntityWithRoles;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class RecoveredChemistry {

	private String pmcid;
	private List<NamedEntityWithRoles> roles;
	private List<NamedEntity> entities;
	private Map<NamedEntity, String> resolvedEntities;
	private int sentenceCount;
	private int prepPhraseCount;
	private int dissolvePhraseCount;
	private int sectionCounter;

	private Map<String,Throwable> crashes;

	public RecoveredChemistry(String pmcid) {
		this.pmcid = pmcid;
		this.sentenceCount = 0;
		this.prepPhraseCount = 0;
		this.dissolvePhraseCount = 0;
		this.sectionCounter = 0;
	}

	public void addNamedEntities(List<NamedEntity> entities) {
		if (this.entities == null)
			this.entities = new ArrayList<NamedEntity>();
		this.entities.addAll(entities);
	}

	public void addResolvedNamedEntities(Map<NamedEntity,String> resolvedEntities) {
		if (this.resolvedEntities == null)
			this.resolvedEntities = new HashMap<NamedEntity, String>();
		this.resolvedEntities.putAll(resolvedEntities);
	}

	public void addRoles(Collection<NamedEntityWithRoles> roles) {
		if (this.roles == null)
			this.roles = new ArrayList<NamedEntityWithRoles>();
		this.roles.addAll(roles);
	}

	public Collection<NamedEntityWithRoles> getRoles() {
		if (this.roles == null) return Collections.emptyList();
		return Collections.unmodifiableCollection(this.roles);
	}

	public List<NamedEntity> getNamedEntities() {
		if (this.entities == null) return Collections.emptyList();
		return Collections.unmodifiableList(entities);
	}

	public Map<NamedEntity,String> getResolvedNamedEntities() {
		if (this.resolvedEntities == null) return Collections.emptyMap();
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

	public void newSection() {
		this.sectionCounter++;
	}

	public int getSectionCount() {
		return this.sectionCounter;
	}

	public Map<String,Throwable> getCrashes() {
		if (this.crashes == null) return Collections.emptyMap();
		return Collections.unmodifiableMap(this.crashes);
	}

	public void addCrash(String text, Throwable crash) {
		if (this.crashes == null)
			this.crashes = new HashMap<String,Throwable>();
		this.crashes.put(text, crash);
	}

}
