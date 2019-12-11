package de.novatec.showcase.manufacture.dto;

import java.util.ArrayList;
import java.util.Collection;

public class Assembly extends Component {

	private static final long serialVersionUID = 1L;

	private Collection<Bom> assemblyBoms;
	
	public Assembly() {
		super();
	}

	public Assembly(String name, String description, String revision, Integer planner, Integer type,
			Integer purchased, Integer lomark, Integer himark) {
		super(name, description, revision, planner, type, purchased, lomark, himark);
		assemblyBoms = new ArrayList<Bom>();
	}

	public Collection<Bom> getAssemblyBoms() {
		return assemblyBoms;
	}

	public void addComponent(Bom bom) {
		this.assemblyBoms.add(bom);
	}

	@Override
	public String toString() {
		return "Assembly [assemblyBoms=" + assemblyBoms + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getDescription()=" + getDescription() + ", getRevision()=" + getRevision() + ", getPlanner()="
				+ getPlanner() + ", getType()=" + getType() + ", getPurchased()=" + getPurchased() + ", getLomark()="
				+ getLomark() + ", getHimark()=" + getHimark() + ", getComponentBoms()=" + getComponentBoms()
				+ ", getVersion()=" + getVersion() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + "]";
	}
}
