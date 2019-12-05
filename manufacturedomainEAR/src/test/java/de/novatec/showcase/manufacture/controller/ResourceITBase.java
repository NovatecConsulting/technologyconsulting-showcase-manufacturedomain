package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public abstract class ResourceITBase {

	
	protected static final String PORT = System.getProperty("http.port");
	protected static final String BASE_URL = "http://localhost:" + PORT + "/manufacturedomain/";

	protected static final String COMPONENT_URL = BASE_URL + "component/";
	protected static final String ASSEMBLY_URL = BASE_URL + "assembly/";
	protected static final String WORKORDER_URL = BASE_URL + "workorder/";

	protected static Client client;
	
	@BeforeClass
	public static void beforeClass() {
		client = ClientBuilder.newClient();
		client.register(JsrJsonpProvider.class);
		client.register(JacksonJsonProvider.class);
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().build();
		client.register(feature);
		setup();
	}
	
	@AfterClass
	public static void teardown() {
		client.close();
	}
	
	protected static void setup()
	{
		//create components
		
		//create assembly
		
		//create boms
		
		//create inventory
	}
	
	// test scheduling a workorder
	// with enough parts
	// with not enough parts
	
	// tests setting the various states of a workorder
	
	// test for completing a workorder	
	
	// test for canceling a workorder
	
	// test for delivering parts for a workorder
	
	// test for inventory get methods (bom/inventory/component/assembly/workorder)
	
	//security test cases allowed/not allowed

	public static void assertResponse200(String url, Response response) {
		assertResponse(url, response, Response.Status.OK);
	}

	public static void assertResponse201(String url, Response response) {
		assertResponse(url, response, Response.Status.CREATED);
	}

	public static void assertResponse404(String url, Response response) {
		assertResponse(url, response, Response.Status.NOT_FOUND);
	}

	public static void assertResponse500(String url, Response response) {
		assertResponse(url, response, Response.Status.INTERNAL_SERVER_ERROR);
	}
	
	public static void assertResponse(String url, Response response, Response.Status status) {
		assertEquals("Incorrect response code from " + url, status.getStatusCode(),
				response.getStatus());
	}
	
	protected static Builder asAdmin(Builder builder) {
		return asUser(builder, "admin", "adminpwd");
	}

	protected static Builder asTestUser(Builder builder) {
		return asUser(builder, "testuser", "pwd");
	}

	protected static Builder asUser(Builder builder, String userName, String password) {
		return builder.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, userName)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password);
	}
}
