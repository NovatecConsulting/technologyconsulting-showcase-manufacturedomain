package de.novatec.showcase.manufacture.dto;

import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="BomPK", description="POJO that represents a BomPK.")
public class BomPK {

	@Schema(required=true)
	private int lineNo;
	@Schema(required=true)
	private String assemblyId;
	@Schema(required=true)
	private String componentId;

	public BomPK() {
		super();
	}

	public BomPK(String componentId, String assemblyId, int lineNo) {
		super();
		this.componentId = componentId;
		this.assemblyId = assemblyId;
		this.lineNo = lineNo;
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
