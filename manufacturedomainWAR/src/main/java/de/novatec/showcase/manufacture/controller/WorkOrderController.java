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

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import de.novatec.showcase.manufacture.GlobalConstants;
import de.novatec.showcase.manufacture.client.supplier.RestcallException;
import de.novatec.showcase.manufacture.dto.WorkOrder;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrderStatus;
import de.novatec.showcase.manufacture.ejb.session.WorkOrderSessionLocal;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@ManagedBean
@Path(value = "/workorder")
@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME, GlobalConstants.WORKORDER_READ_ROLE_NAME})
@Tags(value= {@Tag(name = "WorkOrder")})
public class WorkOrderController {

	@EJB
	protected WorkOrderSessionLocal bean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	@APIResponses(
	    value = {
	    		@APIResponse(
	            responseCode = "404",
	            description = "WorkOrder not found",
	            content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	        @APIResponse(
	        		responseCode = "500",
	        		description = "WorkOrder id is less than 1",
	      		content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	        @APIResponse(
	            responseCode = "200",
	            description = "The WorkOrder with the given id.",
	            content = @Content(mediaType = MediaType.APPLICATION_JSON,
	            schema = @Schema(implementation = WorkOrder.class))) })
	@Operation(
	    summary = "Get the WorkOrder by id",
	    description = "Get the WorkOrder by id where the id has to be higher than 0.")
	public Response getWorkOrder(
			@Parameter(
		        description = "The id of the WorkOrder which should be retrieved.",
		        required = true,
		        example = "1",
		        schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("id") Integer workorderId) {
		if (workorderId.intValue() <= 0) {
			return Response.serverError().entity("Id cannot be less than 1!").type(MediaType.TEXT_PLAIN).build();
		}
		de.novatec.showcase.manufacture.ejb.entity.WorkOrder workOrder = bean.findWorkOrder(workorderId);
		if (workOrder == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("WorkOrder with id '" + workorderId + "' not found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(DtoMapper.mapToWorkOrderDto(workOrder)).type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponses(
	    value = {
	        @APIResponse(
	            responseCode = "404",
	            description = "No WorkOrder found",
	            content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	        @APIResponse(
	            responseCode = "200",
	            description = "The availbler WorkOrder.",
	            content = @Content(mediaType = MediaType.APPLICATION_JSON,
	            schema = @Schema(type = SchemaType.ARRAY, implementation = WorkOrder.class))) })
	@Operation(
	    summary = "Get the WorkOrder",
	    description = "Get the available WorkOrder.")
	public Response getWorkOrders() {
		Collection<WorkOrder> workOrders = DtoMapper.mapToWorkOrderDto(bean.getAllWorkOrders());
		if (workOrders == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No WorkOrder found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(workOrders).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "by_status/{status}")
	@APIResponses(
	    value = {
	        @APIResponse(
	            responseCode = "404",
	            description = "WorkOrders with given WorkOrderStatus not found",
	            content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	        @APIResponse(
	            responseCode = "200",
	            description = "The WorkOrder with the given WorkOrderStatus.",
	            content = @Content(mediaType = MediaType.APPLICATION_JSON,
	            schema = @Schema(implementation = WorkOrder.class))) })
	@Operation(
		summary = "Get the WorkOrders by WorkOrderStatus",
	    description = "Get the WorkOrders by WorkOrderStatus.")
	public Response getWorkOrderbyStatus(
			@Parameter(
		         description = "The WorkOrderStatus of the WorkOrder.",
		         required = true) 
			@PathParam(value = "status") WorkOrderStatus status) {
		Collection<WorkOrder> workOrders = DtoMapper.mapToWorkOrderDto(bean.getWorkOrderByStatus(status));
		if (workOrders == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No WorkOrder found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(workOrders).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@APIResponses(
	    value = {
	    		@APIResponse(
		        responseCode = "500",
		        description = "Rest call to supplier domain purchase has failed",
		        content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	        @APIResponse(
	            responseCode = "201",
	            description = "The new created WorkOrder.",
	            content = @Content(mediaType = MediaType.APPLICATION_JSON,
	            schema = @Schema(implementation = WorkOrder.class))) })
		@RequestBody(
            name="workOrder",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WorkOrder.class)),
            required = true,
            description = "example of a WorkOrder"
        )
	@Operation(
		summary = "Schedule a new WorkOrder",
		description = "Schedule a new WorkOrder from the given suppier object.")
	public Response scheduleWorkOrder(WorkOrder workOrder, @Context UriInfo uriInfo) {
		// TODO validate workOrder (there are some fields which has to be set for an initial workorder - have a look in the constructors or workorder.json)
		Integer id;
		try {
			id = bean.scheduleWorkOrder(DtoMapper.mapToWorkOrderEntity(workOrder));
		} catch (RestcallException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		}
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(id))).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@APIResponses(
	     value = {
		     @APIResponse(
		        	responseCode = "500",
		        	description = "WorkOrder id is less than 1",
		        	content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	         @APIResponse(
	            responseCode = "200",
	            description = "WorkOrder with the given id is canceled.",
	            content = @Content(mediaType = MediaType.APPLICATION_JSON,
	            schema = @Schema(implementation = WorkOrder.class))) })
	@Operation(
	     summary = "Cancel the WorkOrder by id",
	     description = "Cancel the WorkOrder by id where the id has to be higher than 0.")
	public Response cancelWorOrder(
			@Parameter(
		        description = "The id of the WorkOrder which should be canceled.",
		        required = true,
		        example = "1",
		        schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("id") Integer workOrderId, @Context UriInfo uriInfo) {
		if (workOrderId.intValue() <= 0) {
			return Response.serverError().entity("Id cannot be less than 1!").type(MediaType.TEXT_PLAIN).build();
		}
		bean.cancelWorkOrder(workOrderId);
		WorkOrder workOrder = DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(workOrderId));
		return Response.ok(uriInfo.getAbsolutePathBuilder().build()).entity(workOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@Path(value = "{workOrderId}/{manufacturedQuantity}")
	@APIResponses(
	     value = {
		    @APIResponse(
		        	responseCode = "500",
		        	description = "WorkOrder id or manufactured quantity is less than 1",
		        	content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	        @APIResponse(
	            responseCode = "200",
	            description = "WorkOrder with the given id is canceled.",
	            content = @Content(mediaType = MediaType.APPLICATION_JSON,
	            schema = @Schema(implementation = WorkOrder.class))) })
	@Operation(
	    summary = "Complete the WorkOrder by id",
	    description = "Complete the WorkOrder by id with the manufactured quantity where the id and the manufactured quantity has to be higher than 0.")
	public Response completeWorkOrder(
			@Parameter(
		        description = "The id of the WorkOrder which should be completed.",
		        required = true,
		        example = "1",
		        schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("workOrderId")Integer workOrderId, 
			@Parameter(
		        description = "The manufactured quantity of the WorkOrder which should be completed.",
		        required = true,
		        example = "1",
		        schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("manufacturedQuantity")Integer manufacturedQuantity, 
			@Context UriInfo uriInfo) {
		if (workOrderId.intValue() <= 0 || manufacturedQuantity <= 0) {
			return Response.serverError().entity("WorkOrder id or manufactored quantity cannot be less than 1!").type(MediaType.TEXT_PLAIN).build();
		}
		bean.completeWorkOrder(workOrderId, manufacturedQuantity);
		WorkOrder workOrder = DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(workOrderId.intValue()));
		return Response.ok(uriInfo.getAbsolutePathBuilder().build()).entity(workOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(value = "advance_status/{id}")
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@APIResponses(
	     value = {
		    @APIResponse(
		        	responseCode = "500",
		        	description = "WorkOrder id is less than 1",
		        	content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	        @APIResponse(
	            responseCode = "200",
	            description = "WorkOrder with the given id is advanced.",
	            content = @Content(mediaType = MediaType.APPLICATION_JSON,
	            schema = @Schema(implementation = WorkOrder.class))) })
	@Operation(
	    summary = "Advance status of the WorkOrder by id",
	    description = "Advance status of the WorkOrder by id where the id has to be higher than 0.")
	public Response advanceStatus(
			@Parameter(
		            description = "The id of the WorkOrder where the status should be advanced.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("id") Integer workOrderId, @Context UriInfo uriInfo) {
		if (workOrderId.intValue() <= 0) {
			return Response.serverError().entity("Id cannot be less than 1!").type(MediaType.TEXT_PLAIN).build();
		}
		bean.advanceWorkOrderStatus(workOrderId);
		WorkOrder workOrder = DtoMapper.mapToWorkOrderDto(bean.findWorkOrder(workOrderId));
		return Response.ok(uriInfo.getAbsolutePathBuilder().build()).entity(workOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
}
