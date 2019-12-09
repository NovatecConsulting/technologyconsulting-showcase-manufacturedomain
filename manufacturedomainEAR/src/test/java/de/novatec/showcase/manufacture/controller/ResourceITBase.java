package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.novatec.showcase.manufacture.dto.Assembly;
import de.novatec.showcase.manufacture.dto.Bom;
import de.novatec.showcase.manufacture.dto.BomPK;
import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.dto.Inventory;
import de.novatec.showcase.manufacture.dto.InventoryPK;

public abstract class ResourceITBase {

	protected static final String PORT = System.getProperty("http.port");
	protected static final String BASE_URL = "http://localhost:" + PORT + "/manufacturedomain/";

	protected static final String COMPONENT_URL = BASE_URL + "component/";
	protected static final String ASSEMBLY_URL = BASE_URL + "assembly/";
	protected static final String WORKORDER_URL = BASE_URL + "workorder/";

	protected static final String INVENTORY_URL = COMPONENT_URL + "inventory/";
	protected static final String BOM_URL = COMPONENT_URL + "bom/";

	protected static Client client;
	
	// @formatter:off
	private static List<Component> setupComponents = Arrays.asList(
			new Component("Part 1", "The 1st part", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Component("Part 2", "The 2nd part", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Component("Part 3", "The 3rd part", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1))
			);
	private static List<Assembly> setupAssemblies = Arrays.asList(
			new Assembly("Assembly 1", "Assembly 1 which is build from 3 parts", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Assembly("Assembly 2", "Assembly 2 which is build from 2 parts", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1))
			);
	@SuppressWarnings("serial")
	private static Map<String, List<String>> setupBoms = new HashMap<String, List<String>>() {
		{
			put("Assembly 1", Arrays.asList("Part 1", "Part 2", "Part 3"));
			put("Assembly 2", Arrays.asList("Part 1", "Part 3"));
		}
	};
	// @formatter:on

	protected static Map<String, Component> dbComponents = new HashMap<String, Component>();
	protected static Map<String, Assembly> dbAssemblies = new HashMap<String, Assembly>();
	protected static Map<BomPK, Bom> dbBoms = new HashMap<BomPK, Bom>();	
	protected static Map<String, Inventory> dbInventories = new HashMap<String, Inventory>();	
	
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

	private static void setup() {
		for (Component component : setupComponents) {
			Component dbComponent = createComponent(component);
			dbComponents.put(dbComponent.getName(), dbComponent);
		}

		for (Assembly assembly : setupAssemblies) {
			Assembly dbAssembly = createAssembly(assembly);
			dbAssemblies.put(dbAssembly.getName(), dbAssembly);
		}

		for (Entry<String, List<String>>entry : setupBoms.entrySet()) {
			Assembly assembly = dbAssemblies.get(entry.getKey());
			int lineNo = 1;
			for (String componentKey : entry.getValue()) {
				Component component = dbComponents.get(componentKey);
				Bom bom = createBom(lineNo, component, assembly);
				dbBoms.put(bom.getPk(), bom);
				lineNo++;
			}
		}

		for (Entry<String, Component> entry : dbComponents.entrySet()) {
			dbInventories.put(entry.getValue().getName(), createInventoy(entry.getValue()));
		}
		
		for (Entry<String, Assembly> entry : dbAssemblies.entrySet()) {
			dbInventories.put(entry.getValue().getName(), createInventoy(entry.getValue()));
		}

		//reload and store locally each dbComponent/dbAssembly so that we have all connections between objects for comparing in tests!
		for (Entry<String, Component> entry : dbComponents.entrySet()) {
			WebTarget target = client.target(COMPONENT_URL).path(entry.getValue().getId());
			Response response = target.request().get();
			assertResponse200(COMPONENT_URL, response);
			dbComponents.put(entry.getValue().getName(), response.readEntity(Component.class));
		}
		
		for (Entry<String, Assembly> entry : dbAssemblies.entrySet()) {
			WebTarget target = client.target(ASSEMBLY_URL).path(entry.getValue().getId());
			Response response = target.request().get();
			assertResponse200(ASSEMBLY_URL, response);
			dbAssemblies.put(entry.getValue().getName(), response.readEntity(Assembly.class));
		}
	}

	protected static Component createComponent(Component component) {
		WebTarget target = client.target(COMPONENT_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(component));
		assertResponse201(COMPONENT_URL, response);

		target = client.target(COMPONENT_URL)
				.path(Integer.valueOf(response.readEntity(JsonObject.class).getString("id")).toString());
//		response = asAdmin(target.request()).get();
		response = target.request().get();
		assertResponse200(COMPONENT_URL, response);

		return response.readEntity(Component.class);
	}

	protected static Assembly createAssembly(Assembly assembly) {
		WebTarget target = client.target(ASSEMBLY_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(assembly));
		assertResponse201(ASSEMBLY_URL, response);

		target = client.target(ASSEMBLY_URL)
				.path(Integer.valueOf(response.readEntity(JsonObject.class).getString("id")).toString());
//		response = asAdmin(target.request()).get();
		response = target.request().get();
		assertResponse200(ASSEMBLY_URL, response);

		return response.readEntity(Assembly.class);
	}

	protected static Bom createBom(int lineNo, Component component, Assembly assembly) {
		WebTarget target = client.target(BOM_URL);
		Bom bom = new Bom(lineNo, 10, "engChange", 1, "opsDesc", component, assembly, 0);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(bom));
		assertResponse201(BOM_URL, response);

		BomPK bomPK = response.readEntity(BomPK.class);
		target = client.target(BOM_URL).path(Integer.valueOf(bomPK.getLineNo()) + "/"
				+ Integer.valueOf(bomPK.getComponentId()) + "/" + Integer.valueOf(bomPK.getAssemblyId()));
//		response = asAdmin(target.request()).get();
		response = target.request().get();
		assertResponse200(BOM_URL, response);
		bom = response.readEntity(Bom.class);
		
		//connect BOM
		target = client.target(BOM_URL+"/addToComponent/");
//		builder = asAdmin(target.request(MediaType.APPLICATION_JSON));
		builder = target.request(MediaType.APPLICATION_JSON);
		response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(bomPK));
		return bom;
	}

	protected static Inventory createInventoy(Component component) {
		WebTarget target = client.target(INVENTORY_URL);
		Inventory inventory = new Inventory(1, constantDate(), 1, 0, 10, component);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(inventory));
		assertResponse201(INVENTORY_URL, response);

		InventoryPK inventoryPK = response.readEntity(InventoryPK.class);
		target = client.target(INVENTORY_URL)
				.path(inventoryPK.getComponentId()+"/"+inventoryPK.getLocation().toString());
//		response = asAdmin(target.request()).get();
		response = target.request().get();
		assertResponse200(INVENTORY_URL, response);

		return response.readEntity(Inventory.class);
	}

	// test scheduling a workorder
	// with enough parts
	// with not enough parts

	// tests setting the various states of a workorder

	// test for completing a workorder

	// test for canceling a workorder

	// test for delivering parts for a workorder

	// test for get methods (bom/inventory/component/assembly/workorder)

	// security test cases allowed/not allowed

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
		assertEquals("Incorrect response code from " + url, status.getStatusCode(), response.getStatus());
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

	protected static Calendar constantDate() {
		Calendar calendar = Calendar.getInstance(Locale.GERMAN);
		calendar.set(Calendar.YEAR, 2019);
		calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 20);
		return calendar;
	}

}
