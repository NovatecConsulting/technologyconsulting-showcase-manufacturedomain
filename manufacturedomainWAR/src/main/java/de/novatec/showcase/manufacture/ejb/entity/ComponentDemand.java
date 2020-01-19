package de.novatec.showcase.manufacture.ejb.entity;

import java.util.Objects;

public class ComponentDemand {

	private String componentId;
	private int quantity;
	private int location;

	public ComponentDemand() {
		super();
	}

	public ComponentDemand(String componentId, int quantity, int location) {
		super();
		this.componentId = componentId;
		this.quantity = quantity;
		this.location = location;
	}

	public String getComponentId() {
		return componentId;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getLocation() {
		return location;
	}

	@Override
	public int hashCode() {
		return Objects.hash(componentId, location, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ComponentDemand)) {
			return false;
		}
		ComponentDemand other = (ComponentDemand) obj;
		return Objects.equals(componentId, other.componentId) && location == other.location
				&& quantity == other.quantity;
	}

	@Override
	public String toString() {
		return "ComponentDemand [componentId=" + componentId + ", quantity=" + quantity + ", location=" + location
				+ "]";
	}

}
