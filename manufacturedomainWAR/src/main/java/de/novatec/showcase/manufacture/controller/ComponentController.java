package de.novatec.showcase.manufacture.controller;

import java.util.Collection;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
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

import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.dto.ComponentDemand;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@ManagedBean
@Path(value = "/component")
public class ComponentController extends BaseComponentController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	public Response getComponent(@PathParam("id") String componentId) {
		if (Integer.valueOf(componentId).intValue() <= 0) {
			return Response.serverError().entity("Id cannot be less than 1!").build();
		}
		de.novatec.showcase.manufacture.ejb.entity.Component component = bean.findComponent(componentId);
		if (component == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Component with id '" + componentId + "' not found!").build();
		}
		return Response.ok().entity(DtoMapper.mapToComponentDto(component)).type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComponent() {
		Collection<Component> components = DtoMapper.mapToComponentDto(bean.getAllComponents());

		if (components == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Component found!").build();
		}
		return Response.ok().entity(components).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/deliver")
	public Response deliver(List<ComponentDemand> componentDemands) {
		bean.deliver(DtoMapper.mapToComponentDemandEntity(componentDemands));
		return Response.ok().build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
//	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	public Response createComponent(Component component, @Context UriInfo uriInfo) {
		// TODO validate component
		String id = bean.createComponent(DtoMapper.mapToComponentEntity(component));
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("id", id);
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(builder.build()).build();
	}
}
