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

import de.novatec.showcase.manufacture.dto.Assembly;
import de.novatec.showcase.manufacture.dto.ComponentDemand;
import de.novatec.showcase.manufacture.dto.ComponentDemands;

public class AssemblyResouceIT extends ResourceITBase {

	@Test
	public void testGetAssembly() {
		for (Entry<String, Assembly> entry : dbAssemblies.entrySet()) {
			Assembly assembly = entry.getValue();
			WebTarget target = client.target(ASSEMBLY_URL).path(assembly.getId());
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
			assertResponse200(ASSEMBLY_URL, response);
			assertEquals(assembly, response.readEntity(Assembly.class));
		}
	}

	@Test
	public void testGetAssemblies() {
		WebTarget target = client.target(ASSEMBLY_URL);
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		assertResponse200(ASSEMBLY_URL, response);
		assertTrue("There should be 2 Assembly at a minimum!", response.readEntity(new GenericType<List<Assembly>>() {
		}).size() >= 2);
	}

	@Test
	public void testGetAllAssemblyIds() {
		WebTarget target = client.target(ASSEMBLY_URL);
		Response response = target.path("ids").request(MediaType.APPLICATION_JSON_TYPE).get();
		assertResponse200(ASSEMBLY_URL, response);
		assertTrue("There should be 2 Assembly ids at a minimum!", response.readEntity(new GenericType<List<String>>() {
		}).size() >= 2);
	}
	
	@Test
	public void testCreateAssembly() {
		Assembly assembly = new Assembly("Create Assembly Test Part", "The part from testCreateAssembly", "1",
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
		WebTarget target = client.target(ASSEMBLY_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(assembly));
		assertResponse201(ASSEMBLY_URL, response);

		target = client.target(ASSEMBLY_URL)
				.path(Integer.valueOf(response.readEntity(JsonObject.class).getString("id")).toString());
		response = target.request().get();
		assertResponse200(ASSEMBLY_URL, response);
	}
	
	@Test
	public void testDeliveryOfParts() {
		ComponentDemands componentDemands = new ComponentDemands()
				.setComponentDemands(Arrays.asList(new ComponentDemand(dbComponents.get("Part 1").getId(), 10, 1),
						new ComponentDemand(dbComponents.get("Part 2").getId(), 20, 1),
						new ComponentDemand(dbComponents.get("Part 3").getId(), 30, 1)));
		WebTarget target = client.target(ASSEMBLY_URL);
		Builder builder = target.path("/deliver").request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(componentDemands));
		assertResponse200(ASSEMBLY_URL, response);
	}
}
