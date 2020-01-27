package de.novatec.showcase.manufacture.client.supplier;

import java.util.Collection;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.novatec.showcase.manufacture.dto.ComponentDemands;
import de.novatec.showcase.manufacture.dto.PurchaseOrder;
import de.novatec.showcase.manufacture.ejb.entity.ComponentDemand;
import de.novatec.showcase.manufacture.mapper.DtoMapper;

@ManagedBean
public class ComponentDemandPurchaser {

	private static final String JNDI_PROPERTY_SUPPLIERDOMAIN_PURCHASE_URL = "supplierdomain.purchase.url";
	private static final String JNDI_PROPERTY_SUPPLIERDOMAIN_USERNAME = "supplierdomain.username";
	private static final String JNDI_PROPERTY_SUPPLIERDOMAIN_PASSWORD = "supplierdomain.password";
	private static final Logger LOG = LoggerFactory.getLogger(ComponentDemandPurchaser.class);
	private String purchaseUrl;
	private String username;
	private String password;
	private Client client;

	public ComponentDemandPurchaser() throws ManufactureDomainNotConfiguredException {
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().build();
		client.register(feature);

		try {
			purchaseUrl = (String) new InitialContext().lookup(JNDI_PROPERTY_SUPPLIERDOMAIN_PURCHASE_URL);
			username = (String) new InitialContext().lookup(JNDI_PROPERTY_SUPPLIERDOMAIN_USERNAME);
			password = (String) new InitialContext().lookup(JNDI_PROPERTY_SUPPLIERDOMAIN_PASSWORD);
		} catch (NamingException e) {
			LOG.error("JNDI properties " + JNDI_PROPERTY_SUPPLIERDOMAIN_PURCHASE_URL + " or "
					+ JNDI_PROPERTY_SUPPLIERDOMAIN_USERNAME + " or " + JNDI_PROPERTY_SUPPLIERDOMAIN_PASSWORD
					+ " not found! Using system properties where possible!", e);
			throw new ManufactureDomainNotConfiguredException("One or more JNDI properties for the manufacture domain is/are missing!");
		}
		if (validateJNDIProperty(purchaseUrl) || validateJNDIProperty(username) || validateJNDIProperty(password)) {
			throw new ManufactureDomainNotConfiguredException(
					"One or more JNDI properties for the manufacture domain are missing in the server.env file of open liberty!");
		}
	}

	public Collection<PurchaseOrder> purchase(List<ComponentDemand> componentDemands) throws RestcallException {
		WebTarget target = client.target(purchaseUrl);
		Response response = asOrderer(target.request(MediaType.APPLICATION_JSON_TYPE)).post(Entity
				.json(new ComponentDemands().setComponentDemands(DtoMapper.mapToComponentDemandDto(componentDemands))));
		if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
			return response.readEntity(new GenericType<List<PurchaseOrder>>() {
			});
		}
		throw new RestcallException("Error " + Response.Status.fromStatusCode(response.getStatus()) + " while calling "
				+ purchaseUrl + " with " + componentDemands + ". " + response.readEntity(String.class));

	}

	private Builder asOrderer(Builder builder) {
		return asUser(builder, username, password);
	}

	private static Builder asUser(Builder builder, String userName, String password) {
		return builder.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, userName)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password);
	}
	
	private boolean validateJNDIProperty(String value) {
		return value.startsWith("${env.");
	}
}
