package de.novatec.showcase.manufacture.controller;

import java.util.Collection;

import javax.annotation.ManagedBean;
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
import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.ejb.session.ManufactureSessionLocal;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@ManagedBean
@Path(value = "/assembly")
@RolesAllowed({ GlobalConstants.ADMIN_ROLE_NAME, GlobalConstants.COMPONENT_READ_ROLE_NAME })
@Tags(value= {@Tag(name = "Assembly")})
public class AssemblyController {

	@EJB
	protected ManufactureSessionLocal bean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "404",
	                description = "Assembly not found",
	                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	            		responseCode = "500",
	            		description = "Assembly id is less than 1",
	            		content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The Assembly with the given id.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
    @Operation(
            summary = "Get the Assembly",
            description = "Get the Assembly by id.")
	public Response getAssembly(
			@Parameter(
		            description = "The id of the Assembly.",
		            required = true,
		            example = "1",
		            schema = @Schema(type = SchemaType.STRING)) 
			@PathParam("id") String assemblyId) {
		if (Integer.valueOf(assemblyId).intValue() <= 0) {
			return Response.serverError().entity("Id cannot be less than 1!").type(MediaType.TEXT_PLAIN).build();
		}
		de.novatec.showcase.manufacture.ejb.entity.Assembly assembly = bean.findAssembly(assemblyId);
		if (assembly == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Assembly with id '" + assemblyId + "' not found!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(DtoMapper.mapToAssemblyDto(assembly)).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "404",
	                description = "Assembly not found",
	                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The Assembly with the given id.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
    @Operation(
            summary = "Get the assemblies",
            description = "Get the assemblies.")
	public Response getAssemblies() {
		Collection<Assembly> assemblies = DtoMapper.mapToAssemblyDto(bean.getAllAssemblies());

		if (assemblies == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Assembly found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(assemblies).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "ids")
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "404",
	                description = "No Assembly ids found",
	                content = @Content(mediaType = MediaType.TEXT_PLAIN)),
	            @APIResponse(
	                responseCode = "200",
	                description = "The Assembly ids which where found.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
    @Operation(
          summary = "Get the ids of the assemblies",
          description = "Get the ids of the assemblies.")
	public Response getAssemblyIds() {
		Collection<String> assembliesIds = bean.getAllAssemblyIds();
		if (assembliesIds == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Assembly found!").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity(assembliesIds).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	@APIResponses(
	        value = {
	            @APIResponse(
	                responseCode = "201",
	                description = "The new Assembly.",
	                content = @Content(mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Assembly.class))) })
		@RequestBody(
            name="assembly",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Component.class)),
            required = true,
            description = "example of a Assembly obejct."
        )
    @Operation(
            summary = "Create a new Assembly",
            description = "Create a new Assembly by the given Assembly object.")
	public Response createAssembly(Assembly assembly, @Context UriInfo uriInfo) {
		// TODO validate assembly
		String id = bean.createAssembly(DtoMapper.mapToAssemblyEntity(assembly));
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(bean.findAssembly(id)).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

}
