package de.novatec.showcase.manufacture.ejb.entity;

import java.util.Objects;

public class BomPK {

	private int lineNo = -1;
	private String assemblyId;
	private String componentId;
	
	public BomPK()
	{
		super();
	}

	public BomPK(String assemblyId, String componentId, int lineNo) {
		super();
		this.lineNo = lineNo;
		this.assemblyId = assemblyId;
		this.componentId = componentId;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getAssemblyId() {
		return assemblyId;
	}

	public void setAssemblyId(String assemblyId) {
		this.assemblyId = assemblyId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(assemblyId, componentId, lineNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BomPK)) {
			return false;
		}
		BomPK other = (BomPK) obj;
		return Objects.equals(assemblyId, other.assemblyId) && Objects.equals(componentId, other.componentId)
				&& lineNo == other.lineNo;
	}

	@Override
	public String toString() {
		return "BomPK [lineNo=" + lineNo + ", assemblyId=" + assemblyId + ", componentId=" + componentId + "]";
	}
}
