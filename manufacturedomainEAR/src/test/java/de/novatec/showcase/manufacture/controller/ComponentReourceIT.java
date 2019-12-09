package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.dto.ComponentDemand;
import de.novatec.showcase.manufacture.dto.ComponentDemands;

public class ComponentReourceIT extends ResourceITBase {

	@Test
	public void testGetComponent() {
		for (Entry<String, Component> entry : dbComponents.entrySet()) {
			Component component = entry.getValue();
			WebTarget target = client.target(COMPONENT_URL).path(component.getId());
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
			assertResponse200(COMPONENT_URL, response);
			assertEquals(component, response.readEntity(Component.class));
		}
	}

	@Test
	public void testGetComponents() {
		WebTarget target = client.target(COMPONENT_URL);
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		assertResponse200(COMPONENT_URL, response);
		assertTrue("There should be 5 Component at a minimum!", response.readEntity(new GenericType<List<Component>>() {
		}).size() >= 5);
	}

	@Test
	public void testCreateComponent() {
		Component component = new Component("Create Component Test Part", "The part from testCreateComponent", "1",
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
		WebTarget target = client.target(COMPONENT_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(component));
		assertResponse201(COMPONENT_URL, response);

		target = client.target(COMPONENT_URL)
				.path(Integer.valueOf(response.readEntity(JsonObject.class).getString("id")).toString());
		response = target.request().get();
		assertResponse200(COMPONENT_URL, response);
	}

	@Test
	public void testDeliveryOfParts() {
		ComponentDemands componentDemands = new ComponentDemands()
				.setComponentDemands(Arrays.asList(new ComponentDemand(dbComponents.get("Part 1").getId(), 10, 1),
						new ComponentDemand(dbComponents.get("Part 2").getId(), 20, 1),
						new ComponentDemand(dbComponents.get("Part 3").getId(), 30, 1)));
		WebTarget target = client.target(COMPONENT_URL);
		Builder builder = target.path("/deliver").request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(componentDemands));
		assertResponse200(COMPONENT_URL, response);

	}

}
