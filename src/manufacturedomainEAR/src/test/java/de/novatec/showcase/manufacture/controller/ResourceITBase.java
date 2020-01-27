package de.novatec.showcase.manufacture.controller;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.novatec.showcase.manufacture.dto.Assembly;
import de.novatec.showcase.manufacture.dto.Bom;
import de.novatec.showcase.manufacture.dto.BomPK;
import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.dto.Inventory;
import de.novatec.showcase.manufacture.dto.PurchaseOrder;
import de.novatec.showcase.manufacture.dto.PurchaseOrderLine;
import io.netty.handler.codec.http.HttpMethod;

public abstract class ResourceITBase {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceITBase.class);
	protected static final String PORT = System.getProperty("http.port");
	protected static final String BASE_URL = "http://localhost:" + PORT + "/manufacturedomain/";

	protected static final String COMPONENT_URL = BASE_URL + "component/";
	protected static final String ASSEMBLY_URL = BASE_URL + "assembly/";
	protected static final String WORKORDER_URL = BASE_URL + "workorder/";

	protected static final String INVENTORY_URL = COMPONENT_URL + "inventory/";
	protected static final String BOM_URL = COMPONENT_URL + "bom/";

	protected static Client client;
	
	@Rule
	public MockServerRule mockServerRule = new MockServerRule(this, Integer.valueOf(System.getProperty("http.port.supplier")));

	protected MockServerClient mockServerClient;
	
	// @formatter:off
	private static List<Component> setupComponents = Arrays.asList(
			new Component("Part1", "The 1st part", "1", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Component("Part2", "The 2nd part", "1", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Component("Part3", "The 3rd part", "1", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Component("Part4", "The 4th part", "1", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(10), Integer.valueOf(100)),
			new Component("Part5", "The 5th part", "1", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(10), Integer.valueOf(100))
			);
	private static List<Assembly> setupAssemblies = Arrays.asList(
			new Assembly("Assembly1", "Assembly 1 which is build from 3 parts", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Assembly("Assembly2", "Assembly 2 which is build from 2 parts", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)),
			new Assembly("Assembly3", "Assembly 3 which is build from 2 parts", "1", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(10), Integer.valueOf(100))
			);
	@SuppressWarnings("serial")
	private static Map<String, List<String>> setupBoms = new HashMap<String, List<String>>() {
		{
			put("Assembly1", Arrays.asList("Part1", "Part2", "Part3"));
			put("Assembly2", Arrays.asList("Part1", "Part3"));
			put("Assembly3", Arrays.asList("Part4", "Part5"));
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
		client.register(JacksonJsonProvider.class);
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().build();
		client.register(feature);
		setup();
	}

	@AfterClass
	public static void teardown() {
		client.close();
	}

	@Before
	public void before()
	{
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setPoNumber(2);
		purchaseOrder.setSentDate(constantDate().getTime());
		purchaseOrder.setSiteId(26);
		purchaseOrder.setStartDate(new Timestamp(constantDate().getTime().getTime()));
		purchaseOrder.setSupplierId(2);
		purchaseOrder.setVersion(0);
		PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
		purchaseOrderLine.setPoNumber(2);
		purchaseOrderLine.setPolNumber(2);
		purchaseOrderLine.setOutstandingBalance( BigDecimal.valueOf(3515,17));
		purchaseOrderLine.setRequestedDeliveryDate(constantDate().getTime());
		purchaseOrderLine.setLeadtime(55);
		purchaseOrderLine.setDeliveryLocation(1);
		purchaseOrderLine.setOptionalComment("COMMENT_ZCWUIOWHDWESWNWRTMKFHFZZBSYEZHCHBOREDIBUQUBYFMREDRKTNTSIIBLCCAMLUMMILPLCY");
		purchaseOrderLine.setPartNumber("1");
		purchaseOrderLine.setOrderedQuantity(20);
		purchaseOrderLine.setVersion(95);
		purchaseOrder.setPurchaseOrderlines(Arrays.asList(purchaseOrderLine));
		
		try {
			mockServerClient.when(
					new HttpRequest()
			        .withMethod(HttpMethod.POST.toString())
			        .withPath("/supplierdomain/supplier/purchase/")
			)
			.respond(
			    new HttpResponse()
			    	.withStatusCode(Response.Status.CREATED.getStatusCode())
			        .withBody(new ObjectMapper().writeValueAsString(Entity.json(Arrays.asList(purchaseOrder)).getEntity()), org.mockserver.model.MediaType.APPLICATION_JSON)
			);
		} catch (JsonProcessingException e) {
			LOG.error("Could not process Json from object " + purchaseOrder + "!", e);
		}
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
				dbBoms.put(new BomPK(bom.getComponentId(), bom.getAssemblyId(), bom.getLineNo()), bom);
				lineNo++;
			}
		}

		for (Entry<String, Component> entry : dbComponents.entrySet()) {
			dbInventories.put(entry.getValue().getName(), createInventory(entry.getValue()));
		}
		
		for (Entry<String, Assembly> entry : dbAssemblies.entrySet()) {
			dbInventories.put(entry.getValue().getName(), createInventory(entry.getValue()));
		}

		//reload and store locally each dbComponent/dbAssembly so that we have all connections between objects for comparing in tests!
		for (Entry<String, Component> entry : dbComponents.entrySet()) {
			WebTarget target = client.target(COMPONENT_URL).path(entry.getValue().getId());
			Response response = asAdmin(target.request()).get();
			assertResponse200(COMPONENT_URL, response);
			dbComponents.put(entry.getValue().getName(), response.readEntity(Component.class));
		}
		
		for (Entry<String, Assembly> entry : dbAssemblies.entrySet()) {
			WebTarget target = client.target(ASSEMBLY_URL).path(entry.getValue().getId());
			Response response = asAdmin(target.request()).get();
			assertResponse200(ASSEMBLY_URL, response);
			dbAssemblies.put(entry.getValue().getName(), response.readEntity(Assembly.class));
		}
	}

	protected static Component createComponent(Component component) {
		WebTarget target = client.target(COMPONENT_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(component));
		assertResponse201(COMPONENT_URL, response);

		target = client.target(COMPONENT_URL)
				.path(response.readEntity(Component.class).getId()); 
		response = asAdmin(target.request()).get();
		assertResponse200(COMPONENT_URL, response);

		return response.readEntity(Component.class);
	}

	protected static Assembly createAssembly(Assembly assembly) {
		WebTarget target = client.target(ASSEMBLY_URL);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(assembly));
		assertResponse201(ASSEMBLY_URL, response);

		target = client.target(ASSEMBLY_URL)
				.path(Integer.valueOf(response.readEntity(Assembly.class).getId()).toString());
		response = asAdmin(target.request()).get();
		assertResponse200(ASSEMBLY_URL, response);

		return response.readEntity(Assembly.class);
	}

	protected static Bom createBom(int lineNo, Component component, Assembly assembly) {
		WebTarget target = client.target(BOM_URL);
		Bom bom = new Bom(lineNo, 10, "engChange", 1, "opsDesc", component, assembly);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(bom));
		assertResponse201(BOM_URL, response);

		bom = response.readEntity(Bom.class);
		target = client.target(BOM_URL).path(Integer.valueOf(bom.getLineNo()) + "/"
				+ Integer.valueOf(bom.getComponentId()) + "/" + Integer.valueOf(bom.getAssemblyId()));
		response = asTestUser(target.request()).get();
		assertResponse200(BOM_URL, response);
		bom = response.readEntity(Bom.class);
		
		//connect BOM
		target = client.target(BOM_URL+"/addToComponent/");
		builder = target.request(MediaType.APPLICATION_JSON);
		response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(new BomPK(bom.getComponentId(), bom.getAssemblyId(), bom.getLineNo())));
		assertResponse200(BOM_URL, response);
		return bom;
	}

	protected static Inventory createInventory(Component component) {
		WebTarget target = client.target(INVENTORY_URL);
		Inventory inventory = new Inventory(1, constantDate(), 1, 0, 10, component);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		Response response = asAdmin(builder.accept(MediaType.APPLICATION_JSON_TYPE)).post(Entity.json(inventory));
		assertResponse201(INVENTORY_URL, response);

		inventory = response.readEntity(Inventory.class);
		target = client.target(INVENTORY_URL)
				.path(inventory.getComponentId()+"/"+inventory.getLocation().toString());
		response = asTestUser(target.request()).get();
		assertResponse200(INVENTORY_URL, response);

		return response.readEntity(Inventory.class);
	}

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
