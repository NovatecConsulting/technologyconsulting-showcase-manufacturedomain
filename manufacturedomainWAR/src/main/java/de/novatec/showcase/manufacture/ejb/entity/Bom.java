package de.novatec.showcase.manufacture.ejb.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


@Table(name = "M_BOM")
@Entity
@NamedQuery(name = "bomQueryAll", query = Bom.QUERY_ALL)
public class Bom implements Serializable{
	public static final String QUERY_ALL = "Select b From Bom b";
	
	private static final long serialVersionUID = 4984661995893952144L;
	
	@AttributeOverrides({
		@AttributeOverride(name="lineNo", column=@Column(name="B_LINE_NO")),
		@AttributeOverride(name="assemblyId", column=@Column(name="B_ASSEMBLY_ID")),
		@AttributeOverride(name="componentId", column=@Column(name="B_COMP_ID"))
	})
	@EmbeddedId
	private BomPK pk;
	
	@Column(name="B_QTY")
	private int quantity;
	
	@Column(name="B_ENG_CHANGE")
	private String engChange;
	
	@Column(name="B_OPS")
	private int opsNo;
	
	@Column(name="B_OPS_DESC")
	private String opsDesc;
	
	@ManyToOne
	@JoinColumn(name="B_COMP_ID",insertable=false,updatable=false)
	private Component component;
	
	@ManyToOne
	@JoinColumn(name="B_ASSEMBLY_ID",insertable=false,updatable=false)
	private Assembly assembly;
	
	@Version
	@Column(name="B_VERSION")
	private int version;

	public Bom() {
		super();
	}

	public Bom(int lineNo, int quantity, String engChange, int opsNo,
			String opsDesc, Component component, Assembly assembly, int version) {
		super();
		this.pk = new BomPK();
		this.pk.setLineNo(lineNo);
		this.pk.setAssemblyId(assembly.getId());
		this.pk.setComponentId(component.getId());
		this.quantity = quantity;
		this.engChange = engChange;
		this.opsNo = opsNo;
		this.opsDesc = opsDesc;
		this.component = component;
		this.assembly = assembly;
		this.version = version;
	}
	
	public BomPK getPk() {
		return pk;
	}
	
	public void setPk(BomPK pk) {
		this.pk = pk;
	}

	public int getLineNo(){
		return this.pk.getLineNo();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getEngChange() {
		return engChange;
	}

	public void setEngChange(String engChange) {
		this.engChange = engChange;
	}

	public int getOpsNo() {
		return opsNo;
	}

	public void setOpsNo(int opsNo) {
		this.opsNo = opsNo;
	}

	public String getOpsDesc() {
		return opsDesc;
	}

	public void setOpsDesc(String opsDesc) {
		this.opsDesc = opsDesc;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}
	
	public Assembly getAssembly() {
		return assembly;
	}

	public void setAssembly(Assembly assembly) {
		this.assembly = assembly;
	}

	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
//				assembly, component, 
				engChange, opsDesc, opsNo, pk, quantity, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Bom)) {
			return false;
		}
		Bom other = (Bom) obj;
		return 
//				Objects.equals(assembly, other.assembly) && Objects.equals(component, other.component)
//				&& 
				Objects.equals(engChange, other.engChange) && Objects.equals(opsDesc, other.opsDesc)
				&& opsNo == other.opsNo && Objects.equals(pk, other.pk) && quantity == other.quantity
				&& version == other.version;
	}

	@Override
	public String toString() {
		return "Bom [pk=" + pk + ", quantity=" + quantity + ", engChange=" + engChange + ", opsNo=" + opsNo
				+ ", opsDesc=" + opsDesc + 
//				", component=" + component + ", assembly=" + assembly + 
				", version=" + version + "]";
	}
}
