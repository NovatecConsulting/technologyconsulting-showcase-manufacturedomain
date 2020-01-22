package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import de.novatec.showcase.manufacture.dto.Assembly;

public class AssemblyResourceIT extends ResourceITBase {

	protected static final String INVENTORY_URL = ASSEMBLY_URL + "inventory/";
	protected static final String BOM_URL = ASSEMBLY_URL + "bom/";

	@Test
	public void testGetAssembly() {
		for (Entry<String, Assembly> entry : dbAssemblies.entrySet()) {
			Assembly assembly = entry.getValue();
			WebTarget target = client.target(ASSEMBLY_URL).path(assembly.getId());
			Response response = asTestUser(target.request(MediaType.APPLICATION_JSON_TYPE)).get();
			assertResponse200(ASSEMBLY_URL, response);
			assertEquals(assembly, response.readEntity(Assembly.class));
		}
	}

	@Test
	public void testGetAssemblies() {
		WebTarget target = client.target(ASSEMBLY_URL);
		Response response = asTestUser(target.request(MediaType.APPLICATION_JSON_TYPE)).get();
		assertResponse200(ASSEMBLY_URL, response);
		assertTrue("There should be " + dbAssemblies.size() + " Assembly at a minimum!",
				response.readEntity(new GenericType<List<Assembly>>() {
				}).size() >= dbAssemblies.size());
	}

	@Test
	public void testGetAllAssemblyIds() {
		WebTarget target = client.target(ASSEMBLY_URL);
		Response response = asTestUser(target.path("ids").request(MediaType.APPLICATION_JSON_TYPE)).get();
		assertResponse200(ASSEMBLY_URL, response);
		assertTrue("There should be " + dbAssemblies.size() + " Assembly ids at a minimum!",
				response.readEntity(new GenericType<List<String>>() {
				}).size() >= dbAssemblies.size());
	}

	@Test
	public void testCreateAssembly() {
		Assembly assembly = new Assembly("Create Assembly Test Part", "The part from testCreateAssembly", "1",
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
		WebTarget target = client.target(ASSEMBLY_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(assembly));
		assertResponse201(ASSEMBLY_URL, response);

		target = client.target(ASSEMBLY_URL)
				.path(Integer.valueOf(response.readEntity(Assembly.class).getId()).toString());
		response = asTestUser(target.request()).get();
		assertResponse200(ASSEMBLY_URL, response);
	}
}
