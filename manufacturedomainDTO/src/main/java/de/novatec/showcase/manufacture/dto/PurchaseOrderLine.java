package de.novatec.showcase.manufacture.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.novatec.showcase.manufacture.GlobalConstants;


public class PurchaseOrderLine implements Serializable {
	private static final long serialVersionUID = 1L;

	private PurchaseOrderLinePK pk;

	private BigDecimal outstandingBalance;

	@JsonFormat(pattern = GlobalConstants.DATE_FORMAT, locale = "de_DE")
	private Date requestedDeliveryDate;

	private int leadtime;

	private int deliveryLocation;

	private String optionalComment;

	private String partNumber;

	private int orderedQuantity;

	private int version;

	private PurchaseOrder purchaseOrder;

	public PurchaseOrderLine() {
		super();
	}

	public PurchaseOrderLine(Integer polNumber, Integer poNumber, int deliveryLocation, String partNumber, int orderedQuantity,
			BigDecimal outstandingBalance, Date requestedDeliveryDate, String optionalComment, int leadtime, int version,
			PurchaseOrder purchaseOrder) {
		super();
		this.pk = new PurchaseOrderLinePK(polNumber, poNumber);
		this.pk = new PurchaseOrderLinePK();
		this.pk.setPolNumber(polNumber);
		this.pk.setPoNumber(poNumber);
		this.outstandingBalance = outstandingBalance;
		this.requestedDeliveryDate = requestedDeliveryDate;
		this.leadtime = leadtime;
		this.deliveryLocation = deliveryLocation;
		this.optionalComment = optionalComment;
		this.partNumber = partNumber;
		this.orderedQuantity = orderedQuantity;
		this.version = version;
		this.purchaseOrder = purchaseOrder;
	}

	public PurchaseOrderLinePK getPk() {
		return this.pk;
	}

	public void setPk(PurchaseOrderLinePK pk) {
		this.pk = pk;
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

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public PurchaseOrder getPurchaseOrder() {
		return this.purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@Override
	public String toString() {
		return "PurchaseOrderLine [pk=" + pk + ", outstandingBalance=" + outstandingBalance + ", requestedDeliveryDate="
				+ requestedDeliveryDate + ", leadtime=" + leadtime + ", deliveryLocation=" + deliveryLocation
				+ ", optionalComment=" + optionalComment + ", partNumber=" + partNumber + ", orderedQuantity="
				+ orderedQuantity + ", version=" + version + ", purchaseOrder=" + purchaseOrder + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(purchaseOrder, pk, outstandingBalance, requestedDeliveryDate, leadtime, deliveryLocation, optionalComment, partNumber,
				orderedQuantity, version);
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
		return Objects.equals(purchaseOrder, other.purchaseOrder) && Objects.equals(pk, other.pk)
				&& Objects.equals(outstandingBalance, other.outstandingBalance) && Objects.equals(requestedDeliveryDate, other.requestedDeliveryDate)
				&& leadtime == other.leadtime && deliveryLocation == other.deliveryLocation
				&& Objects.equals(optionalComment, other.optionalComment) && Objects.equals(partNumber, other.partNumber)
				&& orderedQuantity == other.orderedQuantity && version == other.version;
	}

}