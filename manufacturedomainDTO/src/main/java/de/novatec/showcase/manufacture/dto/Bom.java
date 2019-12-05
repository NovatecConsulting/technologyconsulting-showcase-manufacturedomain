package de.novatec.showcase.manufacture.dto;

import java.io.Serializable;
import java.util.Objects;

public class Bom implements Serializable {

	private static final long serialVersionUID = 1L;

	private BomPK pk;
	private int quantity;
	private String engChange;
	private int opsNo;
	private String opsDesc;
	private Component component;
	private Assembly assembly;
	private int version;

	public Bom() {
		super();
	}

	public BomPK getPk() {
		return pk;
	}

	public void setPk(BomPK pk) {
		this.pk = pk;
	}

	public int getLineNo() {
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
		return Objects.hash(assembly, component, engChange, opsDesc, opsNo, pk, quantity, version);
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
		return Objects.equals(assembly, other.assembly) && Objects.equals(component, other.component)
				&& Objects.equals(engChange, other.engChange) && Objects.equals(opsDesc, other.opsDesc)
				&& opsNo == other.opsNo && Objects.equals(pk, other.pk) && quantity == other.quantity
				&& version == other.version;
	}

	@Override
	public String toString() {
		return "Bom [pk=" + pk + ", quantity=" + quantity + ", engChange=" + engChange + ", opsNo=" + opsNo
				+ ", opsDesc=" + opsDesc + ", component=" + component + ", assembly=" + assembly + ", version="
				+ version + "]";
	}
}
