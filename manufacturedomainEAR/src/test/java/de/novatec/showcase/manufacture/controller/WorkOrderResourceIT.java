package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import de.novatec.showcase.manufacture.dto.Assembly;
import de.novatec.showcase.manufacture.dto.Bom;
import de.novatec.showcase.manufacture.dto.Inventory;
import de.novatec.showcase.manufacture.dto.WorkOrder;

public class WorkOrderResourceIT extends ResourceITBase {

	@Test
	public void testScheduleWorkOrder() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);

		target = client.target(WORKORDER_URL)
				.path(Integer.valueOf(response.readEntity(WorkOrder.class).getId()).toString());
		response = target.request().get();
		assertResponse200(WORKORDER_URL, response);
	}
	
	@Test
	public void testGetWorkOrders() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		
		target = client.target(WORKORDER_URL);
		response = target.request().get();
		assertResponse200(WORKORDER_URL, response);
		assertTrue("There should be 1 WorkOrder at a minimum!", response.readEntity(new GenericType<List<WorkOrder>>() {
		}).size() >= 1);
	}
	
	@Test
	public void testGetWorkOrderByStatus() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);

		target = client.target(WORKORDER_URL);
		response = target.request().get();
		assertResponse200(WORKORDER_URL, response);
		assertTrue("There should be 1 WorkOrder at a minimum!", response.readEntity(new GenericType<List<WorkOrder>>() {
		}).size() >= 1);
	}
	
	@Test
	public void testScheduleWorkOrderEnoughParts() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly2").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);

		Assembly assembly = dbAssemblies.get("Assembly2");
		Collection<Bom> boms = assembly.getAssemblyBoms();
		for (Bom bom : boms) {
			target = client.target(INVENTORY_URL +bom.getPk().getComponentId()+"/1");
			response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
			assertResponse200(INVENTORY_URL, response);
			Inventory inventory = response.readEntity(Inventory.class);
			assertEquals("Quantity in order should be 0!", 0, inventory.getQuantityInOrder());
			assertEquals("Quantity on Hand should be 0!", 0, inventory.getQuantityOnHand());
		}
	}

	@Test
	public void testScheduleWorkOrderNotEnoughParts() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 2, constantDate(), dbAssemblies.get("Assembly3").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		Assembly assembly = dbAssemblies.get("Assembly3");
		Collection<Bom> boms = assembly.getAssemblyBoms();
		for (Bom bom : boms) {
			target = client.target(INVENTORY_URL +bom.getPk().getComponentId()+"/1");
			response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
			assertResponse200(INVENTORY_URL, response);
			Inventory inventory = response.readEntity(Inventory.class);
			assertEquals("Quantity in order should be 90!", 90, inventory.getQuantityInOrder());
			assertEquals("Quantity on Hand should be -10!", -10, inventory.getQuantityOnHand());
		}
	}

}
