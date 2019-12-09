package de.novatec.showcase.manufacture.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.json.bind.annotation.JsonbTransient;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.novatec.showcase.manufacture.GlobalConstants;

public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;

	private InventoryPK pk;

	@JsonFormat(pattern = GlobalConstants.DATE_FORMAT, locale = "de_DE")
	private Calendar accDate;

	private int accCode;

	private int quantityInOrder;

	private int quantityOnHand;

	private int version;

	private Component component;

	public Inventory() {
		super();
	}

	public Inventory(Integer location, Calendar accDate, int accCode, int quantityInOrder, int quantityOnHand,
			Component component) {
		super();
		this.pk = new InventoryPK(component.getId(), location);
		this.accDate = accDate;
		this.accCode = accCode;
		this.quantityInOrder = quantityInOrder;
		this.quantityOnHand = quantityOnHand;
		this.version = 0;
		this.component = component;
	}

	@JsonbTransient
	public String getComponentId() {
		return this.pk.getComponentId();
	}

	public Component getComponent() {
		return this.component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	@JsonbTransient
	public int getLocation() {
		return this.pk.getLocation();
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

	public int getQuantityOnHand() {
		return quantityOnHand;
	}

	public int getVersion() {
		return this.version;
	}

	public InventoryPK getPk() {
		return pk;
	}

	public void setPk(InventoryPK pk) {
		this.pk = pk;
	}

	public void setQuantityOnHand(int quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accCode, accDate, component, pk, quantityInOrder, quantityOnHand, version);
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
				&& Objects.equals(component, other.component) && Objects.equals(pk, other.pk)
				&& quantityInOrder == other.quantityInOrder && quantityOnHand == other.quantityOnHand
				&& version == other.version;
	}

	@Override
	public String toString() {
		return "Inventory [pk=" + pk + ", accDate=" + accDate + ", accCode=" + accCode + ", quantityInOrder="
				+ quantityInOrder + ", quantityOnHand=" + quantityOnHand + ", version=" + version + ", component="
				+ component + "]";
	}
}
