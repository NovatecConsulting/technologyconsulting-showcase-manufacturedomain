package de.novatec.showcase.manufacture.controller;

import java.util.Collection;

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

import de.novatec.showcase.manufacture.dto.Assembly;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@ManagedBean
@Path(value = "/assembly")
public class AssemblyController extends BaseComponentController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "{id}")
	public Response getAssembly(@PathParam("id") String assemblyId) {
		if (Integer.valueOf(assemblyId).intValue() <= 0) {
			return Response.serverError().entity("Id cannot be less than 1!").build();
		}
		de.novatec.showcase.manufacture.ejb.entity.Assembly assembly = bean.findAssembly(assemblyId);
		if (assembly == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Assembly with id '" + assemblyId + "' not found!")
					.build();
		}
		Collection<de.novatec.showcase.manufacture.ejb.entity.Bom> boms = assembly.getAssemblyBoms();
		System.out.println(boms);
		return Response.ok().entity(DtoMapper.mapToAssemblyDto(assembly)).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAssembly() {
		Collection<Assembly> assemblies = DtoMapper.mapToAssemblyDto(bean.getAllAssemblies());

		if (assemblies == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Assembly found!").build();
		}
		return Response.ok().entity(assemblies).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "ids")
	public Response getAssemblyIds() {
		Collection<String> assembliesIds = bean.getAllAssemblyIds();

		if (assembliesIds == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Assembly found!").build();
		}
		return Response.ok().entity(assembliesIds).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
//	@RolesAllowed({GlobalConstants.ADMIN_ROLE_NAME})
	public Response createAssembly(Assembly assembly, @Context UriInfo uriInfo) {
		// TODO validate assembly
		String id = bean.createAssembly(DtoMapper.mapToAssemblyEntity(assembly));
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("id", id);
		return Response.created(uriInfo.getAbsolutePathBuilder().build()).entity(builder.build()).build();
	}

}
