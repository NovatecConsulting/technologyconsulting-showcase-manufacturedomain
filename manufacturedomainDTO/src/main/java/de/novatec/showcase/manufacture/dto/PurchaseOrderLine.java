package de.novatec.showcase.manufacture.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.novatec.showcase.manufacture.GlobalConstants;

public class PurchaseOrderLine implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer poNumber;

	private Integer polNumber;

	private BigDecimal outstandingBalance;

	@JsonFormat(pattern = GlobalConstants.DATE_FORMAT, locale = "de_DE")
	private Date requestedDeliveryDate;

	private int leadtime;

	private int deliveryLocation;

	private String optionalComment;

	private String partNumber;

	private int orderedQuantity;

	private Integer version;

	public PurchaseOrderLine() {
		super();
	}

	public Integer getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(Integer poNumber) {
		this.poNumber = poNumber;
	}

	public Integer getPolNumber() {
		return polNumber;
	}

	public void setPolNumber(Integer polNumber) {
		this.polNumber = polNumber;
	}

	public BigDecimal getOutstandingBalance() {
		return this.outstandingBalance;
	}

	public void setOutstandingBalance(BigDecimal outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public Date getRequestedDeliveryDate() {
		return this.requestedDeliveryDate;
	}

	public void setRequestedDeliveryDate(Date requestedDeliveryDate) {
		this.requestedDeliveryDate = requestedDeliveryDate;
	}

	public int getLeadtime() {
		return this.leadtime;
	}

	public void setLeadtime(int leadtime) {
		this.leadtime = leadtime;
	}

	public int getDeliveryLocation() {
		return this.deliveryLocation;
	}

	public void setDeliveryLocation(int deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public String getOptionalComment() {
		return this.optionalComment;
	}

	public void setOptionalComment(String optionalComment) {
		this.optionalComment = optionalComment;
	}

	public String getPartNumber() {
		return this.partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public int getOrderedQuantity() {
		return this.orderedQuantity;
	}

	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "PurchaseOrderLine [poNumber=" + poNumber + ", polNumber=" + polNumber + ", outstandingBalance="
				+ outstandingBalance + ", requestedDeliveryDate=" + requestedDeliveryDate + ", leadtime=" + leadtime
				+ ", deliveryLocation=" + deliveryLocation + ", optionalComment=" + optionalComment + ", partNumber="
				+ partNumber + ", orderedQuantity=" + orderedQuantity + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(deliveryLocation, leadtime, optionalComment, orderedQuantity, outstandingBalance,
				partNumber, poNumber, polNumber, requestedDeliveryDate, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PurchaseOrderLine)) {
			return false;
		}
		PurchaseOrderLine other = (PurchaseOrderLine) obj;
		return deliveryLocation == other.deliveryLocation && leadtime == other.leadtime
				&& Objects.equals(optionalComment, other.optionalComment) && orderedQuantity == other.orderedQuantity
				&& Objects.equals(outstandingBalance, other.outstandingBalance)
				&& Objects.equals(partNumber, other.partNumber) && Objects.equals(poNumber, other.poNumber)
				&& Objects.equals(polNumber, other.polNumber)
				&& Objects.equals(requestedDeliveryDate, other.requestedDeliveryDate) && version == other.version;
	}

}