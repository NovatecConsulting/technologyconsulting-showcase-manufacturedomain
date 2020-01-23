package de.novatec.showcase.manufacture.dto;

import java.util.Objects;

import javax.validation.constraints.Size;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="ComponentDemand", description="POJO that represents a ComponentDemand.")
public class ComponentDemand {

	@Schema(required=true)
	@Size(max = 20)
	private String componentId;
	@Schema(required=true)
	private int quantity;
	@Schema(required=true)
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

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setLocation(int location) {
		this.location = location;
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
