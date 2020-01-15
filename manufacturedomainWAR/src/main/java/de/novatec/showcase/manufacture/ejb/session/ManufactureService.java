package de.novatec.showcase.manufacture.ejb.session;

import java.util.Collection;
import java.util.List;

import de.novatec.showcase.manufacture.ejb.entity.Assembly;
import de.novatec.showcase.manufacture.ejb.entity.Bom;
import de.novatec.showcase.manufacture.ejb.entity.BomPK;
import de.novatec.showcase.manufacture.ejb.entity.Component;
import de.novatec.showcase.manufacture.ejb.entity.ComponentDemand;
import de.novatec.showcase.manufacture.ejb.entity.Inventory;
import de.novatec.showcase.manufacture.ejb.entity.InventoryPK;
import de.novatec.showcase.manufacture.ejb.session.exception.InventoryNotFoundException;

public interface ManufactureService {
	Component findComponent(String id);

	Collection<Component> getAllComponents();

	Assembly findAssembly(String id);

	Collection<Assembly> getAllAssemblies();

	Collection<String> getAllAssemblyIds();

	Collection<Bom> getAllBoms();

	Inventory getInventory(String compId, Integer location);

	Collection<Inventory> getAllInventories();

	void deliver(List<ComponentDemand> delivery) throws InventoryNotFoundException;

	String createComponent(Component component);

	String createAssembly(Assembly assembly);

	InventoryPK createInventory(Inventory inventory);

	BomPK createBom(Bom bom);

	Bom findBom(BomPK bomPK);

	void addBomToComponent(BomPK bomPK);
}
