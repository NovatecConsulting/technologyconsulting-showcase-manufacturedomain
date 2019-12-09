package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import de.novatec.showcase.manufacture.dto.Component;

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

}
