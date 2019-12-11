package de.novatec.showcase.manufacture.mapper;

import java.util.Collection;
import java.util.List;

import de.novatec.showcase.manufacture.dto.Assembly;
import de.novatec.showcase.manufacture.dto.Bom;
import de.novatec.showcase.manufacture.dto.BomPK;
import de.novatec.showcase.manufacture.dto.Component;
import de.novatec.showcase.manufacture.dto.ComponentDemand;
import de.novatec.showcase.manufacture.dto.Inventory;
import de.novatec.showcase.manufacture.dto.InventoryPK;
import de.novatec.showcase.manufacture.dto.WorkOrder;
import de.novatec.showcase.manufacture.dto.WorkOrderStatus;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;

abstract public class DtoMapper {
	private static MapperFactory mapperFactory;
	private static MapperFacade mapper;

	static {
		mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.classMap(Component.class, de.novatec.showcase.manufacture.ejb.entity.Component.class ).byDefault().register();
		mapperFactory.classMap(Assembly.class, de.novatec.showcase.manufacture.ejb.entity.Assembly.class ).
		use(Component.class, de.novatec.showcase.manufacture.ejb.entity.Component.class).byDefault().register();
		mapperFactory.classMap(InventoryPK.class, de.novatec.showcase.manufacture.ejb.entity.InventoryPK.class ).byDefault().register();
		mapperFactory.classMap(Inventory.class, de.novatec.showcase.manufacture.ejb.entity.Inventory.class).byDefault().register();
		mapperFactory.classMap(BomPK.class, de.novatec.showcase.manufacture.ejb.entity.BomPK.class ).byDefault().register();
		mapperFactory.classMap(Bom.class, de.novatec.showcase.manufacture.ejb.entity.Bom.class)
		.customize(new CustomMapper <Bom, de.novatec.showcase.manufacture.ejb.entity.Bom >(){

			@Override
			public void mapBtoA(de.novatec.showcase.manufacture.ejb.entity.Bom entity, Bom dto, MappingContext context) {
				dto.setAssembly(null);
				dto.setComponent(null);
				super.mapBtoA(entity, dto, context);
			}
		}).byDefault().register();
		mapperFactory.classMap(WorkOrder.class, de.novatec.showcase.manufacture.ejb.entity.WorkOrder.class).byDefault().register();
		mapperFactory.classMap(WorkOrderStatus.class, de.novatec.showcase.manufacture.ejb.entity.WorkOrderStatus.class).byDefault().register();
		mapper = mapperFactory.getMapperFacade();
	}

	public static Collection<Component> mapToComponentDto(
			Collection<de.novatec.showcase.manufacture.ejb.entity.Component> components) {
		return mapper.mapAsList(components, Component.class);
	}

	public static Collection<Assembly> mapToAssemblyDto(
			Collection<de.novatec.showcase.manufacture.ejb.entity.Assembly> assembly) {
		return mapper.mapAsList(assembly, Assembly.class);
	}

	public static Collection<Bom> mapToBomDto(Collection<de.novatec.showcase.manufacture.ejb.entity.Bom> boms) {
		return mapper.mapAsList(boms, Bom.class);
	}

	public static Collection<Inventory> mapToInventoryDto(
			Collection<de.novatec.showcase.manufacture.ejb.entity.Inventory> inventories) {
		return mapper.mapAsList(inventories, Inventory.class);
	}

	public static Collection<WorkOrder> mapToWorkOrderDto(
			Collection<de.novatec.showcase.manufacture.ejb.entity.WorkOrder> workorders) {
		return mapper.mapAsList(workorders, WorkOrder.class);
	}

	public static Component mapToComponentDto(de.novatec.showcase.manufacture.ejb.entity.Component component) {
		return mapper.map(component, Component.class);
	}

	public static Assembly mapToAssemblyDto(de.novatec.showcase.manufacture.ejb.entity.Assembly assembly) {
		return mapper.map(assembly, Assembly.class);
	}

	public static Inventory mapToInventoryDto(de.novatec.showcase.manufacture.ejb.entity.Inventory inventoryPK) {
		return mapper.map(inventoryPK, Inventory.class);
	}

	public static InventoryPK mapToInventoryPKDto(de.novatec.showcase.manufacture.ejb.entity.InventoryPK inventoryPK) {
		return mapper.map(inventoryPK, InventoryPK.class);
	}
	
	public static BomPK mapToBomPKDto(de.novatec.showcase.manufacture.ejb.entity.BomPK bomPK) {
		return mapper.map(bomPK, BomPK.class);
	}

	public static Bom mapToBomDto(de.novatec.showcase.manufacture.ejb.entity.Bom bom) {
		return mapper.map(bom, Bom.class);
	}
	
	public static WorkOrder mapToWorkOrderDto(de.novatec.showcase.manufacture.ejb.entity.WorkOrder workorder) {
		return mapper.map(workorder, WorkOrder.class);
	}
	
	public static List<de.novatec.showcase.manufacture.ejb.entity.ComponentDemand> mapToComponentDemandEntity(
			List<ComponentDemand> componentDemands) {
		return mapper.mapAsList(componentDemands, de.novatec.showcase.manufacture.ejb.entity.ComponentDemand.class);
	}

	public static de.novatec.showcase.manufacture.ejb.entity.Inventory mapToInventoryEntity(Inventory inventory) {
		return mapper.map(inventory, de.novatec.showcase.manufacture.ejb.entity.Inventory.class);
	}

	public static de.novatec.showcase.manufacture.ejb.entity.Bom mapToBomEntity(Bom bom) {
		return mapper.map(bom, de.novatec.showcase.manufacture.ejb.entity.Bom.class);
	}

	public static de.novatec.showcase.manufacture.ejb.entity.BomPK mapToBomPKEntity(BomPK bom) {
		return mapper.map(bom, de.novatec.showcase.manufacture.ejb.entity.BomPK.class);
	}
	
	public static de.novatec.showcase.manufacture.ejb.entity.Component mapToComponentEntity(Component components) {
		return mapper.map(components, de.novatec.showcase.manufacture.ejb.entity.Component.class);
	}

	public static de.novatec.showcase.manufacture.ejb.entity.Assembly mapToAssemblyEntity(Assembly assembly) {
		return mapper.map(assembly, de.novatec.showcase.manufacture.ejb.entity.Assembly.class);
	}
	
	public static de.novatec.showcase.manufacture.ejb.entity.WorkOrder mapToWorkOrderEntity(WorkOrder workOrder) {
		return mapper.map(workOrder, de.novatec.showcase.manufacture.ejb.entity.WorkOrder.class);
	}
}
