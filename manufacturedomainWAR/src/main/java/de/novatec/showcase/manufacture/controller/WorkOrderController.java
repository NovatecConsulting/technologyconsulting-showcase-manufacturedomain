package de.novatec.showcase.manufacture.controller;

import java.util.Collection;

import javax.annotation.ManagedBean;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.novatec.showcase.manufacture.GlobalConstants;
import de.novatec.showcase.manufacture.client.supplier.RestcallException;
import de.novatec.showcase.manufacture.dto.WorkOrder;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrderStatus;
import de.novatec.showcase.manufacture.ejb.session.WorkOrderSessionLocal;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@ManagedBean
@Path(value = "/workorder")
@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME, GlobalConstants.WORKORDER_READ_ROLE_NAME})
public class WorkOrderController {

	@EJB
	protected WorkOrderSessionLocal bean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	public Response getWorkOrder(@PathParam("id") Integer workorderId) {
		if (Integer.valueOf(workorderId).intValue() <= 0) {
			return Response.serverError().entity("Id cannot be less than 1!").build();
		}
		de.novatec.showcase.manufacture.ejb.entity.WorkOrder workOrder = bean.findWorkOrder(workorderId);
		if (workOrder == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("WorkOrder with id '" + workorderId + "' not found!").build();
		}
		return Response.ok().entity(DtoMapper.mapToWorkOrderDto(workOrder)).type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkOrder() {
		Collection<WorkOrder> workOrders = DtoMapper.mapToWorkOrderDto(bean.getAllWorkOrders());
		if (workOrders == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No WorkOrder found!").build();
		}
		return Response.ok().entity(workOrders).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "by_status/{status}")
	public Response getWorkOrderbyStatus(@PathParam(value = "status") WorkOrderStatus status) {
		Collection<WorkOrder> workOrders = DtoMapper.mapToWorkOrderDto(bean.getWorkOrderByStatus(status));
		if (workOrders == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No WorkOrder found!").build();
		}
		return Response.ok().entity(workOrders).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	public Response scheduleWorkOrder(WorkOrder workOrder, @Context UriInfo uriInfo) {
		// TODO validate workOrder (there are some fields which has to be set for an initial workorder - have a look in the constructors or workorder.json)
		Integer id;
		try {
			id = bean.scheduleWorkOrder(DtoMapper.mapToWorkOrderEntity(workOrder));
		} catch (RestcallException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(id))).build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	public Response cancelWorOrder(@PathParam("id") Integer workorderId, @Context UriInfo uriInfo) {
		bean.cancelWorkOrder(workorderId);
		WorkOrder workOrder = DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(workorderId));
		return Response.ok(uriInfo.getAbsolutePathBuilder().build()).entity(workOrder).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@Path(value = "{workOrderId}/{manufacturedQuantity}")
	public Response completeWorkOrder(
			@PathParam("workOrderId")Integer workOrderId, 
			@PathParam("manufacturedQuantity")Integer manufacturedQuantity, 
			@Context UriInfo uriInfo) {
		bean.completeWorkOrder(workOrderId, manufacturedQuantity);
		WorkOrder workOrder = DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(workOrderId.intValue()));
		return Response.ok(uriInfo.getAbsolutePathBuilder().build()).entity(workOrder).build();
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(value = "advance_status/{id}")
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	public Response advanceStatus(@PathParam("id") Integer workorderId, @Context UriInfo uriInfo) {
		bean.advanceWorkOrderStatus(workorderId);
		WorkOrder workOrder = DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(workorderId));
		return Response.ok(uriInfo.getAbsolutePathBuilder().build()).entity(workOrder).build();
	}
}
