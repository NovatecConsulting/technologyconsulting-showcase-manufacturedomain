package de.novatec.showcase.manufacture.client.supplier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.novatec.showcase.manufacture.dto.ComponentDemands;
import de.novatec.showcase.manufacture.dto.PurchaseOrder;
import de.novatec.showcase.manufacture.ejb.entity.ComponentDemand;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

public class ComponentDemandPurchaser {

	private static final String PORT = System.getProperty("http.port.supplier");
	private static final String BASE_URL = "http://localhost:" + PORT + "/supplierdomain/";

	private static final String SUPPLIER_URL = BASE_URL + "supplier/";
	private static final String PURCHASE_URL = SUPPLIER_URL + "purchase/";
	private Client client;

	public ComponentDemandPurchaser() {
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().build();
		client.register(feature);
	}

	public Collection<PurchaseOrder> purchase(List<ComponentDemand> componentDemands) throws RestcallException {
		WebTarget target = client.target(PURCHASE_URL);
		Response response = asOrderer(target.request(MediaType.APPLICATION_JSON_TYPE))
				.post(Entity.json(new ComponentDemands().setComponentDemands(DtoMapper.mapToComponentDemandDto(componentDemands))));
		if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
			return response.readEntity(new GenericType<List<PurchaseOrder>>() {
			});
		}
		throw new RestcallException(
				"Error " + Response.Status.fromStatusCode(response.getStatus()) + " while calling " + PURCHASE_URL + " with " + componentDemands);

	}

	private static Builder asOrderer(Builder builder) {
		return asUser(builder, "orderer", "pwd");
	}

	private static Builder asUser(Builder builder, String userName, String password) {
		return builder.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, userName)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password);
	}
}
