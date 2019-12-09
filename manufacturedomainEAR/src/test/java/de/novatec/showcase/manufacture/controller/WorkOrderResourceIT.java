package de.novatec.showcase.manufacture.controller;

import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
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

}
