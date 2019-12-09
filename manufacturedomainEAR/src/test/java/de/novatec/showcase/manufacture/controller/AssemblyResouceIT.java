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
import de.novatec.showcase.manufacture.dto.Bom;
import de.novatec.showcase.manufacture.dto.BomPK;
import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.dto.ComponentDemand;
import de.novatec.showcase.manufacture.dto.ComponentDemands;
import de.novatec.showcase.manufacture.dto.Inventory;
import de.novatec.showcase.manufacture.dto.InventoryPK;

public class AssemblyResouceIT extends ResourceITBase {
	
	protected static final String INVENTORY_URL = ASSEMBLY_URL + "inventory/";
	protected static final String BOM_URL = ASSEMBLY_URL + "bom/";


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
	
	@Test
	public void testGetBoms() {
		WebTarget target = client.target(BOM_URL);
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		assertResponse200(BOM_URL, response);
		assertTrue("There should be 5 bom at a minimum!", response.readEntity(new GenericType<List<Bom>>() {
		}).size() >= 5);
	}

	@Test
	public void testGetBom() {
		WebTarget target = client.target(BOM_URL+"/1/1/4");
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		assertResponse200(BOM_URL, response);
		Bom bom = response.readEntity(Bom.class);
		assertEquals("LineNo should be 1", 1, bom.getLineNo());
		assertEquals("Assembly id should be 4", "4", bom.getPk().getAssemblyId());
		assertEquals("Component id should be 1", "1", bom.getPk().getComponentId());
	}
	
	@Test
	public void testCreateBom() {
		Component component = new Component("Create Bom Test Part", "The part from testCreateBom", "1",
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
		Component dbComponent = createComponent(component);
		Assembly assembly = new Assembly("Create Bom Test Assembly", "The assembly from testCreateBom", "1",
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
		Assembly dbAssembly = createAssembly(assembly);
		Bom bom = new Bom(1, 10, "engChange", 1, "opsDesc", dbComponent, dbAssembly, 0);
		
		WebTarget target = client.target(BOM_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(bom));
		assertResponse201(BOM_URL, response);

		BomPK bomPK = response.readEntity(BomPK.class);
		target = client.target(BOM_URL).path(Integer.valueOf(bomPK.getLineNo()) + "/"
				+ Integer.valueOf(bomPK.getComponentId()) + "/" + Integer.valueOf(bomPK.getAssemblyId()));
		response = target.request().get();
		assertResponse200(BOM_URL, response);

		target = client.target(BOM_URL+"/addToComponent/");
//		builder = asAdmin(target.request(MediaType.APPLICATION_JSON));
		builder = target.request(MediaType.APPLICATION_JSON);
		response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(bomPK));
		assertResponse200(BOM_URL, response);
	}


@Test
	public void testGetAllInventories() {
		WebTarget target = client.target(INVENTORY_URL);
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		assertResponse200(INVENTORY_URL, response);
		assertTrue("There should be 5 inventories at a minimum!", response.readEntity(new GenericType<List<Inventory>>() {
		}).size() >= 5);
	}
	
	@Test
	public void testGetInventory() {
		WebTarget target = client.target(INVENTORY_URL+"/1/1");
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		assertResponse200(INVENTORY_URL, response);
		Inventory inventory = response.readEntity(Inventory.class);
		assertEquals("Component id should be 1", "1", inventory.getPk().getComponentId());
		assertEquals("Location id should be 1", Integer.valueOf(1), inventory.getPk().getLocation());
	}

	@Test
	public void testCreateInventory() {
		Component component = new Component("Create Inventory Test Part", "The part from testCreateInventory", "1",
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
		Component dbComponent = createComponent(component);
		Assembly assembly = new Assembly("Create Inventory Test Assembly", "The assembly from testCreateInventory", "1",
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
		Assembly dbAssembly = createAssembly(assembly);
		createBom(1, dbComponent, dbAssembly);

		Inventory inventory = new Inventory(1, constantDate(), 1, 0, 10, dbComponent);

		WebTarget target = client.target(INVENTORY_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(inventory));
		assertResponse201(INVENTORY_URL, response);

		InventoryPK inventoryPK = response.readEntity(InventoryPK.class);
		target = client.target(INVENTORY_URL)
				.path(inventoryPK.getComponentId() + "/" + inventoryPK.getLocation().toString());
		response = target.request().get();
		assertResponse200(INVENTORY_URL, response);
	}
	

}
