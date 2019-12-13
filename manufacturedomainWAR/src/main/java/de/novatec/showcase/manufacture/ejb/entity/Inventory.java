package de.novatec.showcase.manufacture.ejb.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Table(name = "M_INVENTORY")
@Entity
@NamedQuery(name = Inventory.ALL_INVENTORIES, query = Inventory.ALL_INVENTORIES_QUERY)
public class Inventory implements Serializable{

	private static final long serialVersionUID = -5262453330017334196L;
	
	public static final String ALL_INVENTORIES = "ALL_INVENTORIES";
	
	public static final String ALL_INVENTORIES_QUERY = "SELECT i FROM Inventory i";
	
	@AttributeOverrides({
		@AttributeOverride(name="componentId", column=@Column(name="IN_P_ID")),
		@AttributeOverride(name="location", column=@Column(name="IN_LOCATION"))
	})
	@EmbeddedId
	private InventoryPK pk;
	
	@Column(name="IN_ACT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar accDate;
	
	@Column(name="IN_ACC_CODE")
	private int accCode;
	
	@Column(name="IN_ORDERED")
	private int quantityInOrder;
	
	@Column(name="IN_QTY")
	private int quantityOnHand;
	
	@Version
	@Column(name="IN_VERSION")
	private int version;
	
	@ManyToOne
	@JoinColumn(name="IN_P_ID",insertable=false,updatable=false)
	private Component component;
	
	public Inventory() {
		super();
	}

	public Inventory(Integer location, Calendar accDate, int accCode,
			int quantityInOrder, int quantityOnHand, Component component) {
		super();
		this.pk = new InventoryPK();
		this.pk.setComponentId(component.getId());
		this.pk.setLocation(location);
		this.accDate = accDate;
		this.accCode = accCode;
		this.quantityInOrder = quantityInOrder;
		this.quantityOnHand = quantityOnHand;
		this.version = 0;
		this.component = component;
	}
	
	public String getComponentId(){
		return this.pk.getComponentId();
	}
	
	public Component getComponent(){
		return this.component;
	}
	
	public void setComponent(Component component) {
		this.component = component;
	}

	public int getLocation(){
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
	
	public void addQuantityInOrder(int amount) {
		this.quantityInOrder += amount;
	}
	
	public void reduceQuantityInOrder(int amount){
		this.quantityInOrder -= amount;
	}

	public int getQuantityOnHand() {
		return quantityOnHand;
	}

	public void addQuantityOnHand(int amount) {
		this.quantityOnHand += amount;
	}
	
	public void reduceQuantityOnHand(int amount){
		this.quantityOnHand -= amount;
	}
	
	public int getVersion(){
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
