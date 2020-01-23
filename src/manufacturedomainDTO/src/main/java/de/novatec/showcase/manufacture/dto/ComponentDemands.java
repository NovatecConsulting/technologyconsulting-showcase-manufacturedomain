package de.novatec.showcase.manufacture.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="ComponentDemands", description="POJO that represents a list of ComponentDemands.")
public class ComponentDemands {
	
	@Schema(required=true)
	@NotNull
	private List<ComponentDemand> componentDemands;

	public List<ComponentDemand> getComponentDemands() {
		return componentDemands;
	}

	public ComponentDemands setComponentDemands(@Valid List<ComponentDemand> componentDemands) {
		this.componentDemands = componentDemands;
		return this;
	}


}
