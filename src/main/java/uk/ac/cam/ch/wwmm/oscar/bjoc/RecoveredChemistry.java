package uk.ac.cam.ch.wwmm.oscar.bjoc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;

public class RecoveredChemistry {

	private String pmcid;
	private HashMap<String, String> roles;
	private List<NamedEntity> entities;
	private Map<NamedEntity, String> resolvedEntities;

	public RecoveredChemistry(String pmcid) {
		this.pmcid = pmcid;
	}

	public void setNamedEntities(List<NamedEntity> entities) {
		this.entities = entities;
	}

	public void setResolvedNamedEntities(Map<NamedEntity,String> resolvedEntities) {
		this.resolvedEntities = resolvedEntities;
	}

	public void setRoles(HashMap<String,String> roles) {
		this.roles = roles;
	}

	public Map<String,String> getRoles() {
		return Collections.unmodifiableMap(this.roles);
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
}
