package de.novatec.showcase.manufacture.ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

@Table(name = "M_PARTS")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "P_TYPE", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "0")
@NamedQuery(name = Component.ALL_COMPONENTS, query = Component.ALL_COMPONENTS_QUERY)
public class Component implements Serializable {
	
	private static final long serialVersionUID = 2816997595652914797L;
	
	public static final String ALL_COMPONENTS = "ALL_COMPONENTS";
	
	public static final String ALL_COMPONENTS_QUERY = "SELECT c FROM Component c";


	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "C_ID_GEN")
	@TableGenerator(name = "C_ID_GEN", table = "U_SEQUENCES", pkColumnName = "S_ID", valueColumnName = "S_NEXTNUM", pkColumnValue = "C_SEQ", allocationSize = 1)
	private String id;
	@Column(name = "P_NAME")
	private String name;
	@Column(name = "P_DESC")
	private String description;
	@Column(name = "P_REV")
	private String revision;
	@Column(name = "P_PLANNER")
	private Integer planner;
	@Column(name = "P_TYPE")
	private Integer type;
	@Column(name = "P_IND")
	private Integer purchased;
	@Column(name = "P_LOMARK")
	private Integer lomark;

	@Column(name = "P_HIMARK")
	private Integer himark;

	@OneToMany(mappedBy = "component", fetch = FetchType.EAGER)
	private Collection<Bom> componentBoms;

	@Version
	@Column(name = "P_VERSION")
	private int version;

	public Component() {
		super();
	}

	public Component(String id, String name, String description, String revision, Integer planner, Integer type,
			Integer purchased, Integer lomark, Integer himark) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.revision = revision;
		this.planner = planner;
		this.type = type;
		this.purchased = purchased;
		this.lomark = lomark;
		this.himark = himark;
		this.version = 0;
		this.componentBoms = new ArrayList<Bom>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Integer getPlanner() {
		return planner;
	}

	public void setPlanner(Integer planner) {
		this.planner = planner;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPurchased() {
		return purchased;
	}

	public void setPurchased(Integer purchased) {
		this.purchased = purchased;
	}

	public Integer getLomark() {
		return lomark;
	}

	public void setLomark(Integer lomark) {
		this.lomark = lomark;
	}

	public Integer getHimark() {
		return himark;
	}

	public void setHimark(Integer himark) {
		this.himark = himark;
	}

	public Collection<Bom> getComponentBoms() {
		return componentBoms;
	}

	public void addComponentBoms(Collection<Bom> componentBoms) {
		this.componentBoms.addAll(componentBoms);
	}

	public int getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(componentBoms, description, himark, id, lomark, name, planner, purchased, revision, type,
				version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Component)) {
			return false;
		}
		Component other = (Component) obj;
		return Objects.equals(componentBoms, other.componentBoms) && Objects.equals(description, other.description)
				&& Objects.equals(himark, other.himark) && Objects.equals(id, other.id)
				&& Objects.equals(lomark, other.lomark) && Objects.equals(name, other.name)
				&& Objects.equals(planner, other.planner) && Objects.equals(purchased, other.purchased)
				&& Objects.equals(revision, other.revision) && Objects.equals(type, other.type)
				&& version == other.version;
	}

	@Override
	public String toString() {
		return "Component [id=" + id + ", name=" + name + ", description=" + description + ", revision=" + revision
				+ ", planner=" + planner + ", type=" + type + ", purchased=" + purchased + ", lomark=" + lomark
				+ ", himark=" + himark + ", componentBoms=" + componentBoms + ", version=" + version + "]";
	}
}
