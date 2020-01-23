package de.novatec.showcase.manufacture.dto;

import java.util.Objects;

import javax.validation.constraints.Size;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Schema(name="Bom", description="POJO that represents a Bom.")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bom {

	@Size(max = 20)
	private String componentId;
	@Size(max = 20)
	private String assemblyId;
	private int lineNo;
	private int quantity;
	@Size(max = 10)
	private String engChange;
	private int opsNo;
	@Size(max = 100)
	private String opsDesc;
	
	private Integer version;

	public Bom() {
		super();
	}
	
	public Bom(int lineNo, int quantity, String engChange, int opsNo,
			String opsDesc, Component component, Assembly assembly) {
		super();
		this.lineNo = lineNo;
		this.assemblyId = assembly.getId();
		this.componentId = component.getId();
		this.quantity = quantity;
		this.engChange = engChange;
		this.opsNo = opsNo;
		this.opsDesc = opsDesc;
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

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public void setAssemblyId(String assemblyId) {
		this.assemblyId = assemblyId;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(assemblyId, componentId, engChange, lineNo, opsDesc, opsNo, quantity, version);
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
		return Objects.equals(assemblyId, other.assemblyId) && Objects.equals(componentId, other.componentId)
				&& Objects.equals(engChange, other.engChange) && lineNo == other.lineNo
				&& Objects.equals(opsDesc, other.opsDesc) && opsNo == other.opsNo && quantity == other.quantity
				&& version == other.version;
	}

	@Override
	public String toString() {
		return "Bom [componentId=" + componentId + ", assemblyId=" + assemblyId + ", lineNo=" + lineNo + ", quantity="
				+ quantity + ", engChange=" + engChange + ", opsNo=" + opsNo + ", opsDesc=" + opsDesc + ", version="
				+ version + "]";
	}
}
