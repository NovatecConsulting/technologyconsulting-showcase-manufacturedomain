package de.novatec.showcase.manufacture.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.novatec.showcase.manufacture.GlobalConstants;

@Schema(name="Inventory", description="POJO that represents a Inventory.")
public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;

	private String componentId;

	private Integer location;

	@JsonFormat(pattern = GlobalConstants.DATE_FORMAT, locale = "de_DE")
	private Calendar accDate;

	private int accCode;

	private int quantityInOrder;

	private int quantityOnHand;

	private Integer version;

	private Component component;

	public Inventory() {
		super();
	}

	public Inventory(Integer location, Calendar accDate, int accCode, int quantityInOrder, int quantityOnHand,
			Component component) {
		super();
		this.componentId = component.getId();
		this.location = location;
		this.accDate = accDate;
		this.accCode = accCode;
		this.quantityInOrder = quantityInOrder;
		this.quantityOnHand = quantityOnHand;
		this.component = component;
	}

	public String getComponentId() {
		return this.componentId;
	}

	public Integer getLocation() {
		return this.location;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}

	public Calendar getAccDate() {
		return accDate;
	}

	public void setAccDate(Calendar accDate) {
		this.accDate = accDate;
	}

	public int getAccCode() {
		return accCode;
	}

	public void setAccCode(int accCode) {
		this.accCode = accCode;
	}

	public int getQuantityInOrder() {
		return quantityInOrder;
	}

	public void setQuantityInOrder(int quantityInOrder) {
		this.quantityInOrder = quantityInOrder;
	}

	public void setQuantityOnHand(int quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}

	public int getQuantityOnHand() {
		return quantityOnHand;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setQuantityOnHand(Integer quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accCode, accDate, componentId, location, quantityInOrder, quantityOnHand, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Inventory)) {
			return false;
		}
		Inventory other = (Inventory) obj;
		return accCode == other.accCode && Objects.equals(accDate, other.accDate)
				&& Objects.equals(componentId, other.componentId) && Objects.equals(location, other.location)
				&& quantityInOrder == other.quantityInOrder && quantityOnHand == other.quantityOnHand
				&& version == other.version;
	}

	@Override
	public String toString() {
		return "Inventory [componentId=" + componentId + ", location=" + location + ", accDate=" + accDate
				+ ", accCode=" + accCode + ", quantityInOrder=" + quantityInOrder + ", quantityOnHand=" + quantityOnHand
				+ ", version=" + version + ", component=" + component + "]";
	}

}
