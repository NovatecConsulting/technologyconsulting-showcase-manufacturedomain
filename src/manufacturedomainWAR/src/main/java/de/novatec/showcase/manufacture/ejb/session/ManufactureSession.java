package de.novatec.showcase.manufacture.ejb.session;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.novatec.showcase.manufacture.ejb.entity.Assembly;
import de.novatec.showcase.manufacture.ejb.entity.Bom;
import de.novatec.showcase.manufacture.ejb.entity.BomPK;
import de.novatec.showcase.manufacture.ejb.entity.Component;
import de.novatec.showcase.manufacture.ejb.entity.ComponentDemand;
import de.novatec.showcase.manufacture.ejb.entity.Inventory;
import de.novatec.showcase.manufacture.ejb.entity.InventoryPK;
import de.novatec.showcase.manufacture.ejb.session.exception.AssemblyNotFoundException;
import de.novatec.showcase.manufacture.ejb.session.exception.InventoryNotFoundException;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ManufactureSession implements ManufactureSessionLocal {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Component findComponent(String componentId) {
		return em.find(Component.class, componentId);
	}

	@Override
	public Collection<Component> getAllComponents() {
		return em.createNamedQuery(Component.ALL_COMPONENTS, Component.class).getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Assembly findAssembly(String componentId) throws AssemblyNotFoundException {
		Assembly assembly = em.find(Assembly.class, componentId);
		return assembly;
	}

	@Override
	public Collection<Assembly> getAllAssemblies() {
		return em.createNamedQuery(Assembly.ALL_ASSEMBLIES, Assembly.class).getResultList();
	}

	@Override
	public Collection<String> getAllAssemblyIds() {
		return em.createNamedQuery(Assembly.ALL_ASSEMBLY_IDS, String.class).getResultList();
	}

	@Override
	public Bom findBom(BomPK bomPK) {
		return em.find(Bom.class, bomPK);
	}

	@Override
	public Collection<Bom> getAllBoms() {
		return em.createNamedQuery(Bom.ALL_BOMS, Bom.class).getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Inventory getInventory(String componentId, Integer location) {
		return em.find(Inventory.class, new InventoryPK(componentId, location));
	}

	@Override
	public Collection<Inventory> getAllInventories() {
		return em.createNamedQuery(Inventory.ALL_INVENTORIES, Inventory.class).getResultList();
	}

	/**
	 * Inserts delivered components into the Inventory.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deliver(List<ComponentDemand> componentDemands) throws InventoryNotFoundException {
		for (ComponentDemand componentDemand : componentDemands) {
			Inventory inventory = this.getInventory(componentDemand.getComponentId(), componentDemand.getLocation());
			if(inventory != null)
			{
				inventory.addQuantityOnHand(componentDemand.getQuantity());
				// reduce quantity in order
				if (inventory.getQuantityInOrder() - componentDemand.getQuantity() < 0) {
					inventory.reduceQuantityInOrder(inventory.getQuantityInOrder());
				} else {
					inventory.reduceQuantityInOrder(componentDemand.getQuantity());
				}
				inventory.setAccDate(Calendar.getInstance());
			}
			else
			{
				throw new InventoryNotFoundException("Inventory for Component with Id "+ componentDemand.getComponentId() + " and location " + componentDemand.getLocation() +" not found!");
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String createComponent(Component component) {
		em.persist(component);
		return component.getId();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Assembly createAssembly(Assembly assembly) {
		em.persist(assembly);
		return assembly;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Inventory createInventory(Inventory inventory) {
		if (this.findComponent(inventory.getComponentId()) != null) {
			em.persist(inventory);
			return inventory;
		} else {
			return null;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Bom createBom(Bom bom) throws AssemblyNotFoundException {
		if (this.findComponent(bom.getComponentId()) != null
				&& this.findAssembly(bom.getAssemblyId()) != null) {
			em.persist(bom);
			return bom;
		} else {
			return null;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void addBomToComponent(BomPK bomPK) throws AssemblyNotFoundException {
		Component component = this.findComponent(bomPK.getComponentId());
		Assembly assembly = this.findAssembly(bomPK.getAssemblyId());
		Bom bom = this.findBom(bomPK);
		if (component != null && assembly != null && bom != null) {
			component.addComponentBoms(Arrays.asList(bom));
			assembly.addComponent(bom);
		}
		return;
	}
	
}
