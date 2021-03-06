package de.novatec.showcase.manufacture.controller;

import java.util.Collection;

import javax.annotation.ManagedBean;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilderException;
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
import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.ejb.session.exception.ComponentNotFoundException;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@ManagedBean
@Path(value = "/component")
@Tags(value = { @Tag(name = "Component") })
public class ComponentResource extends BaseComponentResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	@APIResponses(value = {
			@APIResponse(responseCode = "404", description = "Component not found", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
			@APIResponse(responseCode = "400", description = "Component id is less than 1", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
			@APIResponse(responseCode = "200", description = "The Component with the given id.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Component.class))) })
	@Operation(summary = "Get the Component", description = "Get the Component by id.")
	public Response getComponent(
			@Parameter(description = "The id of the Component.", required = true, example = "1", schema = @Schema(type = SchemaType.STRING)) @PathParam("id") String componentId) {
		if (Integer.valueOf(componentId).intValue() <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Id cannot be less than 1!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		Component component;
		try {
			component = DtoMapper.mapToComponentDto(bean.findComponent(componentId));
		} catch (ComponentNotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Component with id '" + componentId + "' not found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(component).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponses(value = {
			@APIResponse(responseCode = "404", description = "Component not found", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
			@APIResponse(responseCode = "200", description = "The Component with the given id.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Component.class))) })
	@Operation(summary = "Get the components", description = "Get the components.")
	public Response getComponents() {
		Collection<Component> components = DtoMapper.mapToComponentDto(bean.getAllComponents());

		if (components.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Component found!").type(MediaType.TEXT_PLAIN)
					.build();
		}
		return Response.ok().entity(components).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ GlobalConstants.ADMIN_ROLE_NAME })
	@APIResponses(value = {
			@APIResponse(responseCode = "400", description = "Component is not valid", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
			@APIResponse(responseCode = "201", description = "The new Component.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Component.class))) })
	@RequestBody(name = "component", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Component.class)), required = true, description = "example of a Component obejct.")
	@Operation(summary = "Create a new Component", description = "Create a new Component by the given Component object.")
	public Response createComponent(@Valid Component component, @Context UriInfo uriInfo)
			throws IllegalArgumentException, UriBuilderException, ComponentNotFoundException {
		return Response.created(uriInfo.getAbsolutePathBuilder().build())
				.entity(bean.createComponent(DtoMapper.mapToComponentEntity(component)))
				.type(MediaType.APPLICATION_JSON_TYPE).build();
	}


	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@APIResponses(
	        value = {
 		       @APIResponse(
 			    		responseCode = "400",
 			            description = "Component id is less than 1",
 			            content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
		                responseCode = "404",
		                description = "Component with the given id not found",
		                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The component with the given id was deleted if found.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Component.class))) })
	    @Operation(
	        summary = "Delete component by id",
	        description = "Delete the component with the given id if it is found.")
	public Response deleteComponent(
			@Parameter(
		            description = "The id of the component which should be deleted.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.INTEGER)) 
			@PathParam("id") Integer componentId) {
		if (componentId.intValue() <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Component id cannot be less than 1!").type(MediaType.TEXT_PLAIN_TYPE).build();
		}
		try {
			return Response.ok().entity(DtoMapper.mapToComponentDto(bean.deleteComponent(componentId))).type(MediaType.APPLICATION_JSON_TYPE).build();
		} catch (ComponentNotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
		}
	}

}

