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
@NamedQueries(value = { @NamedQuery(name = Assembly.ALL_ASSEMBLIES, query = Assembly.ALL_ASSEMBLIES_QUERY),
		@NamedQuery(name = Assembly.ALL_ASSEMBLY_IDS, query = Assembly.ALL_ASSEMBLY_IDS_QUERY) })
public class Assembly extends Component {

	private static final long serialVersionUID = -185086822159684535L;
	
	public static final String ALL_ASSEMBLIES = "ALL_ASSEMBLIES";

	public static final String ALL_ASSEMBLY_IDS = "ALL_ASSEMBLY_IDS";

	public static final String ALL_ASSEMBLIES_QUERY = "Select a From Assembly a";
	
	public static final String ALL_ASSEMBLY_IDS_QUERY = "Select a.id From Assembly a";


	@OneToMany(mappedBy = "assembly",fetch = FetchType.EAGER)
	private Collection<Bom> assemblyBoms;

	public Assembly() {
		super();
	}

	public Assembly(String id, String name, String description, String revision, Integer planner, Integer type,
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

}
