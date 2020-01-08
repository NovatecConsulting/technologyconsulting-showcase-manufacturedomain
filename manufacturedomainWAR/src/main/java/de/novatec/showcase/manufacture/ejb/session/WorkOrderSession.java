package de.novatec.showcase.manufacture.ejb.session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.novatec.showcase.manufacture.GlobalConstants;
import de.novatec.showcase.manufacture.client.supplier.ComponentDemandPurchaser;
import de.novatec.showcase.manufacture.client.supplier.RestcallException;
import de.novatec.showcase.manufacture.dto.PurchaseOrder;
import de.novatec.showcase.manufacture.ejb.entity.Assembly;
import de.novatec.showcase.manufacture.ejb.entity.Bom;
import de.novatec.showcase.manufacture.ejb.entity.ComponentDemand;
import de.novatec.showcase.manufacture.ejb.entity.Inventory;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrder;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrderStatus;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class WorkOrderSession implements WorkOrderSessionLocal {

	private static Logger log = LoggerFactory.getLogger(WorkOrderSession.class);

	@PersistenceContext
	private EntityManager em;

	@EJB
	private ManufactureSessionLocal manufactureSession;
	
	private ComponentDemandPurchaser componentDemandPurchaser = new ComponentDemandPurchaser();

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public WorkOrder findWorkOrder(Integer wordOrderId) {
		return em.find(WorkOrder.class, wordOrderId);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<WorkOrder> getAllWorkOrders() {
		return em.createNamedQuery(WorkOrder.ALL_WORKORDERS, WorkOrder.class).getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<WorkOrder> getWorkOrderByStatus(WorkOrderStatus status) {
		TypedQuery<WorkOrder> queryWorkOrdersByStatus = em.createNamedQuery(WorkOrder.WORKORDERS_BY_STATUS, WorkOrder.class);
		queryWorkOrdersByStatus.setParameter("status", status);
		return queryWorkOrdersByStatus.getResultList();
	}

	@Override
	public int scheduleWorkOrder(WorkOrder workOrder) throws RestcallException {
		workOrder.setStartDate(Calendar.getInstance());
		em.persist(workOrder);
		Assembly mfgAssembly = manufactureSession.findAssembly(workOrder.getAssemblyId());
		// get (and remove) required parts from inventory and order new parts if needed
		List<ComponentDemand> componentDemands = new ArrayList<ComponentDemand>();
		for (Bom bom : mfgAssembly.getAssemblyBoms()) {
			int requiredQuantity = bom.getQuantity() * workOrder.getOriginalQuantity();
			Inventory inventory = manufactureSession.getInventory(bom.getComponent().getId(), workOrder.getLocation());
			int orderQuantity = this.getQuantityToBeOrdered(inventory, requiredQuantity);
			if (isBelowWaterMark(orderQuantity)) {
				componentDemands
						.add(new ComponentDemand(bom.getComponent().getId(), orderQuantity, inventory.getLocation()));
				inventory.addQuantityInOrder(orderQuantity);
			}
			inventory.reduceQuantityOnHand(requiredQuantity);
		}

		// send order
		if (componentDemands.size() > 0) {
			if(!GlobalConstants.IS_SINGLE_EAR_DEPLOYMENT)
			{
				Collection<PurchaseOrder> purchaseOrders = componentDemandPurchaser.purchase(componentDemands);
				for (PurchaseOrder purchaseOrder : purchaseOrders) {
					log.info("purchased: " + purchaseOrder);
				}
			}
			else
			{
				log.info("ManufacturdomainEAR is deployed as a single EAR -> calls to supplierdomain.purchase are ignored!");
			}
		}
		return workOrder.getId();
	}

	/**
	 * @return 0: nothing needs to be done<br>
	 *         >0: quantity which must be ordered
	 */
	private int getQuantityToBeOrdered(Inventory inventory, int requiredQuantity) {
		// check low water mark
		if ((inventory.getQuantityInOrder() + inventory.getQuantityOnHand() - requiredQuantity) < inventory.getComponent()
				.getLomark()) {
			return inventory.getComponent().getHimark() - inventory.getQuantityInOrder()
					- inventory.getQuantityOnHand();
		}
		if ((inventory.getQuantityOnHand() - requiredQuantity) < 0) {
			// should rollback the txn
			throw new RuntimeException("##### error in inventory: compId:" + inventory.getComponentId() + " location:"
					+ inventory.getLocation() + " -> not enough parts available");
		}
		return 0;
	}

	@Override
	public void cancelWorkOrder(Integer workOrderId) {
		WorkOrder workOrder = this.findWorkOrder(workOrderId);
		if (isCancelable(workOrder)) {
			Assembly assembly = manufactureSession.findAssembly(workOrder.getAssemblyId());
			for (Bom bom : assembly.getAssemblyBoms()) {
				int compQuantity = bom.getQuantity() * workOrder.getOriginalQuantity();
				this.returnInventory(bom, compQuantity, workOrder.getLocation(), workOrder.getStatus());
			}
			workOrder.setStatusCancelled();
		}
	}

	@Override
	public void completeWorkOrder(Integer workOrderId, int manufacturedQuantity) {
		WorkOrder workOrder = this.findWorkOrder(workOrderId);
		if (isCompletable(workOrder)) {
			workOrder.setStatusCompleted();
			workOrder.setCompletedQuantity(manufacturedQuantity);
			if (doesNotBelongToOrderFromOrderDomain(workOrder)) {
				Inventory inventory = manufactureSession.getInventory(workOrder.getAssemblyId(), workOrder.getLocation());
				inventory.addQuantityOnHand(manufacturedQuantity);
			}
		}
	}

	@Override
	public void advanceWorkOrderStatus(Integer workOrderId) {
		WorkOrder workOrder = this.findWorkOrder(workOrderId);

		if (isAdvanceable(workOrder)) {

			// advance the WorkOrder to the next Stage
			// see WorkOrderStatus for the order of the steps
			workOrder.advanceStatus();

			if (workOrder.getStatus().equals(WorkOrderStatus.STAGE1)) {
				workOrder.setStartDate(Calendar.getInstance());
			}
		}
	}

	private boolean isCancelable(WorkOrder workOrder) {
		return workOrder.getStatus() != WorkOrderStatus.COMPLETED && workOrder.getStatus() != WorkOrderStatus.CANCELED
				&& workOrder.getStatus() != WorkOrderStatus.ARCHIVED;
	}

	/**
	 * returns <code>quantity</code> items of the Component referenced in
	 * <code>Bom</code> to the <code>Inventory</code> at <code>location</code> if
	 * they weren't used yet
	 */
	private void returnInventory(Bom bom, int quantity, int location, WorkOrderStatus workOrderStatus) {
		Inventory inventory = manufactureSession.getInventory(bom.getComponent().getId(), location);
		if (isComponentUsed(bom, workOrderStatus)) {
			inventory.addQuantityOnHand(quantity);
		}
	}

	private boolean doesNotBelongToOrderFromOrderDomain(WorkOrder workOrder) {
		return workOrder.getSalesId() == -1 || workOrder.getOrderLineId() == -1;
	}

	private boolean isCompletable(WorkOrder workOrder) {
		return !workOrder.getStatus().equals(WorkOrderStatus.COMPLETED)
				&& !workOrder.getStatus().equals(WorkOrderStatus.CANCELED)
				&& !workOrder.getStatus().equals(WorkOrderStatus.ARCHIVED);
	}

	/**
	 * canceled or archived: WorkOrder must stay in this status stage3: call
	 * completeWorkOrder() for completion
	 */
	private boolean isAdvanceable(WorkOrder workOrder) {
		return !workOrder.getStatus().equals(WorkOrderStatus.CANCELED)
				&& !workOrder.getStatus().equals(WorkOrderStatus.ARCHIVED)
				&& !workOrder.getStatus().equals(WorkOrderStatus.STAGE3);
	}

	private boolean isComponentUsed(Bom bom, WorkOrderStatus workOrderStatus) {
		return bom.getOpsNo() > workOrderStatus.ordinal();
	}

	private boolean isBelowWaterMark(int orderQuantity) {
		return orderQuantity > 0;
	}

}
