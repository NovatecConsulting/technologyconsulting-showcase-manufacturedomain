package de.novatec.showcase.manufacture.controller;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
import de.novatec.showcase.manufacture.dto.Assembly;
import de.novatec.showcase.manufacture.dto.Bom;
import de.novatec.showcase.manufacture.dto.BomPK;
import de.novatec.showcase.manufacture.dto.ComponentDemands;
import de.novatec.showcase.manufacture.dto.Inventory;
//import de.novatec.showcase.manufacture.dto.InventoryPK;
import de.novatec.showcase.manufacture.ejb.session.ManufactureSessionLocal;
import de.novatec.showcase.manufacture.ejb.session.exception.InventoryNotFoundException;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@RolesAllowed({ GlobalConstants.ADMIN_ROLE_NAME, GlobalConstants.COMPONENT_READ_ROLE_NAME })
public abstract class BaseComponentController {

	@EJB
	protected ManufactureSessionLocal bean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/bom")
	@Tags(value= {@Tag(name = "Bom")})
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "404",
	                description = "No Bom found",
	                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The Boms which are availabale.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
    @Operation(
	    summary = "Get the Boms",
	    description = "Get all available Boms.")
	public Response getAllBoms() {
		Collection<Bom> boms = DtoMapper.mapToBomDto(bean.getAllBoms());
		if (boms == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Boms found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(boms).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/bom/{lineNo}/{componentId}/{assemblyId}")
	@Tags(value= {@Tag(name = "Bom")})
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "404",
	                description = "Bom not found",
	                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The Bom with the given component id, assembly id and lineNo.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
    @Operation(
	    summary = "Get the Bom",
	    description = "Get Bom by lineNo, componentId and assemblyId.")
	public Response getBom(
			@Parameter(
		            description = "The lineNo of the Bom.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("lineNo") Integer lineNo, 
			@Parameter(
		            description = "The componentId of the Bom.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.STRING)) 
			@PathParam("componentId") String componentId,
			@Parameter(
		            description = "The assemblyId of the Bom.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.STRING)) 
			@PathParam("assemblyId") String assemblyId) {
		BomPK bomPK = new BomPK(componentId,assemblyId, lineNo);
		Bom bom = DtoMapper.mapToBomDto(bean.findBom(DtoMapper.mapToBomPKEntity(bomPK)));
		if (bom == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Bom with BomPK '" + bomPK + "' found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(bom).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "inventory/{componentId}/{location}")
	@Tags(value= {@Tag(name = "Inventory")})
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "404",
	                description = "Inventory not found",
	                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
		 		@APIResponse(
			 		responseCode = "500",
			 		description = "ComponentId/location has to be greater than 0.",
			 		content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The Inventory with the given component id and location.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
    @Operation(
	    summary = "Get the Inventory",
	    description = "Get Inventory by componentId and location.")
	public Response getInventory(
			@Parameter(
		            description = "The componentId of the Inventory.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.STRING)) 
			@PathParam("componentId") String componentId,
			@Parameter(
		            description = "The location of the Inventory.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("location") Integer location) {
		if (Integer.valueOf(componentId).intValue() <= 0) {
			return Response.serverError().entity("Component id cannot be less than 1!").type(MediaType.TEXT_PLAIN).build();
		}
		if (location.intValue() <= 0) {
			return Response.serverError().entity("location cannot be less than 1!").type(MediaType.TEXT_PLAIN).build();
		}
		Inventory inventory = DtoMapper.mapToInventoryDto(bean.getInventory(componentId, location));
		if (inventory == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Inventory with component/assembly id '"
					+ componentId + "' and location '" + location + "' has no inventory!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(inventory).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/inventory")
	@Tags(value= {@Tag(name = "Inventory")})
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "404",
	                description = "No Inventories found",
	                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The Inventories which are availabale.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
    @Operation(
    	summary = "Get all Inventories",
    	description = "Get all available Inventories.")
	public Response getInventories() {
		Collection<Inventory> inventories = DtoMapper.mapToInventoryDto(bean.getAllInventories());
		if (inventories == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Inventories found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(inventories).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ GlobalConstants.ADMIN_ROLE_NAME })
	@Path(value = "/inventory")
	@Tags(value= {@Tag(name = "Inventory")})
	@APIResponses(
	        value = {
	 		   @APIResponse(
	 				responseCode = "404",
	 			    description = "The Component with the given id in the Inventory object does not exist.",
	 			    content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	           @APIResponse(
	                responseCode = "201",
	                description = "The new Inventory for the given Inventory object.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Inventory.class))) })
		@RequestBody(
            name="inventory",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Inventory.class)),
            required = true,
            description = "example of a Inventory"
        )
    @Operation(
        summary = "Create a new Inventory",
        description = "Create a new Inventory by the given Inventory object.")
	public Response createInventory(Inventory inventory, @Context UriInfo uriInfo) {
		Inventory createdInventory = DtoMapper
				.mapToInventoryDto(bean.createInventory(DtoMapper.mapToInventoryEntity(inventory)));
		if (createdInventory == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Component with id '" + inventory.getComponentId() + "' not found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(createdInventory).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ GlobalConstants.ADMIN_ROLE_NAME })
	@Path(value = "/bom")
	@Tags(value= {@Tag(name = "Bom")})
	@APIResponses(
	        value = {
	 		   @APIResponse(
	 				responseCode = "404",
	 			    description = "The Component/Assembly with the given id in the Bom object does not exist.",
	 			    content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	           @APIResponse(
	                responseCode = "201",
	                description = "The new Bom for the given inventory object.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Bom.class))) })
		@RequestBody(
            name="bom",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Bom.class)),
            required = true,
            description = "example of a Bom"
        )
    @Operation(
        summary = "Create a new Bom",
        description = "Create a new Bom by the given Bom object.")
	public Response createBom(Bom bom, @Context UriInfo uriInfo) {
		Bom createdBom = DtoMapper.mapToBomDto(bean.createBom(DtoMapper.mapToBomEntity(bom)));
		if (createdBom == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Component with id '" + bom.getComponentId() + "' or " + "Assembly with id '"
							+ bom.getAssemblyId() + "' not found!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(createdBom).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ GlobalConstants.ADMIN_ROLE_NAME })
	@Path(value = "/bom/addToComponent")
	@Tags(value= {@Tag(name = "Bom")})
	@APIResponses(
	        value = {
	 		   @APIResponse(
	 				responseCode = "406",
	 			    description = "No such Bom with the given BomPK was found.",
	 			    content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	           @APIResponse(
	                responseCode = "200",
	                description = "The Bom was added to the Component.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = BomPK.class))) })
		@RequestBody(
            name="bom",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = BomPK.class)),
            required = true,
            description = "example of a BomPk"
        )
    @Operation(
        summary = "Add Bom to Component",
        description = "Add the Bom to Comonent by the given BomPK.")
	public Response addBomToComponent(BomPK bomPK) {
		if (bean.findBom(DtoMapper.mapToBomPKEntity(bomPK)) != null) {
			bean.addBomToComponent(DtoMapper.mapToBomPKEntity(bomPK));
			return Response.ok().build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ GlobalConstants.ADMIN_ROLE_NAME })
	@Path(value = "/deliver")
	@APIResponses(
	    value = {
		 	 @APIResponse(
			 	responseCode = "404",
			    description = "The Inventory or Component does not exist for the delivered ComponentDemand.",
			    content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	         @APIResponse(
	            responseCode = "200",
	            description = "The ComponentDemands are dilvered.")})
		@RequestBody(
            name="componantDemands",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ComponentDemands.class)),
            required = true,
            description = "example of a ComponentDemands"
        )
    @Operation(
        summary = "Deliver ComponentDemands",
        description = "Deliver the ComponentDemands.")
	public Response deliver(ComponentDemands componentDemands) {
		try {
			bean.deliver(DtoMapper.mapToComponentDemandEntity(componentDemands.getComponentDemands()));
		} catch (InventoryNotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().build();
	}
}
