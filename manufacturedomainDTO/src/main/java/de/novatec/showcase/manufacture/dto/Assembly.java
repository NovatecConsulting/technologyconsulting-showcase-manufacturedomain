package de.novatec.showcase.manufacture.dto;

import java.util.ArrayList;
import java.util.Collection;

public class Assembly extends Component {

	private static final long serialVersionUID = 1L;

	private Collection<Bom> assemblyBoms;

	public Assembly() {
		super();
	}

	public Assembly(String id, String name, String description, String revision, Integer planner, Integer type,
			Integer purchased, Integer lomark, Integer himark) {
		super(id, name, description, revision, planner, type, purchased, lomark, himark);
		assemblyBoms = new ArrayList<Bom>();
	}

	public Collection<Bom> getAssemblyBoms() {
		return assemblyBoms;
	}

	public void addComponent(Bom bom) {
		this.assemblyBoms.add(bom);
	}
}
