package de.novatec.showcase.manufacture.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Assembly", description="POJO that represents a Assembly.")
public class Assembly extends Component {

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
		return "Assembly [assemblyBoms=" + assemblyBoms + ", id=" + getId() + ", name=" + getName()
				+ ", description=" + getDescription() + ", revision=" + getRevision() + ", planner="
				+ getPlanner() + ", type=" + getType() + ", purchased=" + getPurchased() + ", lomark="
				+ getLomark() + ", himark=" + getHimark() + ", componentBoms=" + getComponentBoms()
				+ ", version=" + getVersion() + "]";
	}
}
