package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.HttpMethod;
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
import de.novatec.showcase.manufacture.dto.WorkOrderStatus;

public class WorkOrderResourceIT extends ResourceITBase {

	@Test
	public void testScheduleWorkOrder() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);

		target = client.target(WORKORDER_URL)
				.path(Integer.valueOf(response.readEntity(WorkOrder.class).getId()).toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
	}

	@Test
	public void testGetWorkOrders() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);

		target = client.target(WORKORDER_URL);
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertTrue("There should be 1 WorkOrder at a minimum!", response.readEntity(new GenericType<List<WorkOrder>>() {
		}).size() >= 1);
	}

	@Test
	public void testGetWorkOrderByStatus() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);

		target = client.target(WORKORDER_URL);
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertTrue("There should be 1 WorkOrder at a minimum!", response.readEntity(new GenericType<List<WorkOrder>>() {
		}).size() >= 1);
	}

	@Test
	public void testScheduleWorkOrderEnoughParts() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly2").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);

		Assembly assembly = dbAssemblies.get("Assembly2");
		Collection<Bom> boms = assembly.getAssemblyBoms();
		for (Bom bom : boms) {
			target = client.target(INVENTORY_URL + bom.getComponentId() + "/1");
			response = asTestUser(target.request(MediaType.APPLICATION_JSON_TYPE)).get();
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
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		Assembly assembly = dbAssemblies.get("Assembly3");
		Collection<Bom> boms = assembly.getAssemblyBoms();
		for (Bom bom : boms) {
			target = client.target(INVENTORY_URL + bom.getComponentId() + "/1");
			response = asTestUser(target.request(MediaType.APPLICATION_JSON_TYPE)).get();
			assertResponse200(INVENTORY_URL, response);
			Inventory inventory = response.readEntity(Inventory.class);
			assertEquals("Quantity in order should be 90!", 90, inventory.getQuantityInOrder());
			assertEquals("Quantity on Hand should be -10!", -10, inventory.getQuantityOnHand());
		}
	}

	@Test
	public void testAdvancingStatusFromOpenToStage3() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state OPEN", WorkOrderStatus.OPEN, workOrder.getStatus());

		target = client.target(WORKORDER_URL).path("advance_status/" + workOrder.getId());
		response = asAdmin(target.request()).method(HttpMethod.PUT);
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state STAGE1", WorkOrderStatus.STAGE1, workOrder.getStatus());

		target = client.target(WORKORDER_URL).path("advance_status/" + workOrder.getId());
		response = asAdmin(target.request()).method(HttpMethod.PUT);
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state STAGE2", WorkOrderStatus.STAGE2, workOrder.getStatus());

		target = client.target(WORKORDER_URL).path("advance_status/" + workOrder.getId());
		response = asAdmin(target.request()).method(HttpMethod.PUT);
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state STAGE3", WorkOrderStatus.STAGE3, workOrder.getStatus());

		// should stay in WorkOrderStatus.STAGE3
		target = client.target(WORKORDER_URL).path("advance_status/" + workOrder.getId());
		response = asAdmin(target.request()).method(HttpMethod.PUT);
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state STAGE3", WorkOrderStatus.STAGE3, workOrder.getStatus());
	}

	@Test
	public void testCompleteWorkOrder() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		workOrder.setStatus(WorkOrderStatus.STAGE3);
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state OPEN", WorkOrderStatus.STAGE3, workOrder.getStatus());

		// now complete the workorder
		target = client.target(WORKORDER_URL).path(workOrder.getId() + "/" + workOrder.getOriginalQuantity());
		response = asAdmin(target.request()).method(HttpMethod.PUT);
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state COMPLETED", WorkOrderStatus.COMPLETED, workOrder.getStatus());
	}

	@Test
	public void testNotCompleteableWorkOrder() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		workOrder.setStatus(WorkOrderStatus.CANCELED);
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state OPEN", WorkOrderStatus.CANCELED, workOrder.getStatus());

		// now complete the workorder
		target = client.target(WORKORDER_URL).path(workOrder.getId() + "/" + workOrder.getOriginalQuantity());
		response = asAdmin(target.request()).method(HttpMethod.PUT);
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state CANCELED", WorkOrderStatus.CANCELED, workOrder.getStatus());
	}

	@Test
	public void testCancelWorkOrder() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		workOrder.setStatus(WorkOrderStatus.STAGE3);
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state OPEN", WorkOrderStatus.STAGE3, workOrder.getStatus());

		// now complete the workorder
		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asAdmin(target.request()).delete();
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);

		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state CANCELED", WorkOrderStatus.CANCELED, workOrder.getStatus());
	}

	@Test
	public void testNotCancelableWorkOrder() {
		WorkOrder workOrder = new WorkOrder(1, 1, 1, 1, constantDate(), dbAssemblies.get("Assembly1").getId());
		workOrder.setStatus(WorkOrderStatus.COMPLETED);
		WebTarget target = client.target(WORKORDER_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(workOrder));
		assertResponse201(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);
		
		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state OPEN", WorkOrderStatus.COMPLETED, workOrder.getStatus());
		
		// now complete the workorder
		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asAdmin(target.request()).delete();
		assertResponse200(WORKORDER_URL, response);
		workOrder = response.readEntity(WorkOrder.class);
		
		target = client.target(WORKORDER_URL).path(workOrder.getId().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(WORKORDER_URL, response);
		assertEquals("Workorder should be in state CANCELED", WorkOrderStatus.COMPLETED, workOrder.getStatus());
	}
}
