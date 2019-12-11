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
import javax.ws.rs.core.UriInfo;

import de.novatec.showcase.manufacture.GlobalConstants;
import de.novatec.showcase.manufacture.dto.Bom;
import de.novatec.showcase.manufacture.dto.BomPK;
import de.novatec.showcase.manufacture.dto.ComponentDemands;
import de.novatec.showcase.manufacture.dto.Inventory;
import de.novatec.showcase.manufacture.dto.InventoryPK;
import de.novatec.showcase.manufacture.ejb.session.ManufactureSessionLocal;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME, GlobalConstants.COMPONENT_READ_ROLE_NAME})
public abstract class BaseComponentController {

	@EJB
	protected ManufactureSessionLocal bean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/bom")
	public Response getAllBoms() {
		Collection<Bom> boms = DtoMapper.mapToBomDto(bean.getAllBoms());
		if (boms == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Boms found!").build();
		}
		return Response.ok().entity(boms).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/bom/{lineNo}/{componentId}/{assemblyId}")
	public Response getBom(@PathParam("lineNo") Integer lineNo, @PathParam("componentId") String componentId, @PathParam("assemblyId") String assemblyId) {
		BomPK bomPK = new BomPK();
		bomPK.setLineNo(lineNo);
		bomPK.setAssemblyId(assemblyId);
		bomPK.setComponentId(componentId);
		Bom bom = DtoMapper.mapToBomDto(bean.findBom(DtoMapper.mapToBomPKEntity(bomPK)));
		if (bom == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Bom with BomPK '"+bomPK+"' found!").build();
		}
		return Response.ok().entity(bom).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "inventory/{componentId}/{location}")
	public Response getInventory(@PathParam("componentId") String componentId,
			@PathParam("location") Integer location) {
		if (Integer.valueOf(componentId).intValue() <= 0) {
			return Response.serverError().entity("Customer id cannot be less than 1!").build();
		}
		if (location.intValue() <= 0) {
			return Response.serverError().entity("location cannot be less than 1!").build();
		}
		Inventory inventory = DtoMapper.mapToInventoryDto(bean.getInventory(componentId, location));
		if (inventory == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Inventory with component/assembly id '"
					+ componentId + "' and location '" + location + "' has no inventory!").build();
		}
		return Response.ok().entity(inventory).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/inventory")
	public Response getAllInventories() {
		Collection<Inventory> inventories = DtoMapper.mapToInventoryDto(bean.getAllInventories());
		if (inventories == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Inventories found!").build();
		}
		return Response.ok().entity(inventories).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@Path(value = "inventory")
	public Response createInventory(Inventory inventory, @Context UriInfo uriInfo) {
		InventoryPK inventoryPK = DtoMapper.mapToInventoryPKDto(bean.createInventory(DtoMapper.mapToInventoryEntity(inventory)));
		if (inventoryPK == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Component with id '" + inventory.getComponentId() + "' not found!").build();
		}
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(inventoryPK).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@Path(value = "/bom")
	public Response createBom(Bom bom, @Context UriInfo uriInfo) {
		BomPK bomPK = DtoMapper.mapToBomPKDto(bean.createBom(DtoMapper.mapToBomEntity(bom)));
		if (bomPK == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Component with id '" + bom.getPk().getComponentId() + "' or " + "Assembly with id '"
							+ bom.getPk().getAssemblyId() + "' not found!")
					.build();
		}
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(bomPK).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@Path(value = "/bom/addToComponent")
	public Response addBomToComponent(BomPK bomPK) {
		bean.addBomToComponent(DtoMapper.mapToBomPKEntity(bomPK));
		return Response.ok().build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@Path(value = "/deliver")
	public Response deliver(ComponentDemands componentDemands) {
		bean.deliver(DtoMapper.mapToComponentDemandEntity(componentDemands.getComponentDemands()));
		return Response.ok().build();
	}

}
