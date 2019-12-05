package de.novatec.showcase.manufacture.dto;

import java.io.Serializable;
import java.util.Objects;

public class BomPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private int lineNo;
	private String assemblyId;
	private String componentId;

	public BomPK() {
		super();
		this.lineNo = -1;
		this.assemblyId = null;
		this.componentId = null;
	}

	public BomPK(int lineNo, String assemblyId, String componentId) {
		super();
		this.lineNo = lineNo;
		this.assemblyId = assemblyId;
		this.componentId = componentId;
	}

	public int getLineNo() {
		return this.lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getAssemblyId() {
		return this.assemblyId;
	}

	public void setAssemblyId(String assemblyId) {
		this.assemblyId = assemblyId;
	}

	public String getComponentId() {
		return this.componentId;
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
