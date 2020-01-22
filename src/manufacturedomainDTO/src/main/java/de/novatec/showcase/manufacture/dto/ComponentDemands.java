package de.novatec.showcase.manufacture.dto;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="ComponentDemands", description="POJO that represents a list of ComponentDemands.")
public class ComponentDemands {
	
	@Schema(required=true)
	private List<ComponentDemand> componentDemands;

	public List<ComponentDemand> getComponentDemands() {
		return componentDemands;
	}

	public ComponentDemands setComponentDemands(List<ComponentDemand> componentDemands) {
		this.componentDemands = componentDemands;
		return this;
	}


}
