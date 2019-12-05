package de.novatec.showcase.manufacture.ejb.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value = "1")
@NamedQueries(value = { @NamedQuery(name = "assemblyQueryAll", query = Assembly.QUERY_ALL),
		@NamedQuery(name = "assemblyQueryAllIds", query = Assembly.QUERY_ALL_IDS) })
public class Assembly extends Component {

	public static final String QUERY_ALL = "Select a From Assembly a";
	public static final String QUERY_ALL_IDS = "Select a.id From Assembly a";

	private static final long serialVersionUID = -185086822159684535L;

	@OneToMany(mappedBy = "assembly",fetch = FetchType.EAGER)
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
