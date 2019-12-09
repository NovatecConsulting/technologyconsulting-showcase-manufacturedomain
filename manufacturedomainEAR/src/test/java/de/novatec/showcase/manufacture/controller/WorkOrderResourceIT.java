package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

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
				.path(Integer.valueOf(response.readEntity(JsonObject.class).getInt("id")).toString());
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
	
	// get Boms of assembly
	// get inventory
	// get inventory quantity in order
	// get inventory quantity on hand
	// schedule workorder
	// re-read all inventory and check quantities in order and on hand
	

}
