package de.novatec.showcase.manufacture.dto;

import java.io.Serializable;
import java.util.Objects;

public class PurchaseOrderLinePK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer polNumber;

	private Integer poNumber;

	public PurchaseOrderLinePK() {
		super();
	}

	public PurchaseOrderLinePK(Integer polNumber, Integer poNumber) {
		super();
		this.polNumber = polNumber;
		this.poNumber = poNumber;
	}

	public Integer getPolNumber() {
		return this.polNumber;
	}

	public void setPolNumber(Integer polNumber) {
		this.polNumber = polNumber;
	}

	public Integer getPoNumber() {
		return this.poNumber;
	}

	public void setPoNumber(Integer polNumber) {
		this.poNumber = polNumber;
	}

	@Override
	public String toString() {
		return "PurchaseOrderLinePK [polNumber=" + polNumber + ", poNumber=" + poNumber + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(polNumber, poNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PurchaseOrderLinePK)) {
			return false;
		}
		PurchaseOrderLinePK other = (PurchaseOrderLinePK) obj;
		return polNumber == other.polNumber && poNumber == other.poNumber;
	}
}