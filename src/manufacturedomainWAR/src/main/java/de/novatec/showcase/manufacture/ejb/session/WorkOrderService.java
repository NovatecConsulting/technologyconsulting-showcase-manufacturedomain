package de.novatec.showcase.manufacture.ejb.session;

import java.util.Collection;

import de.novatec.showcase.manufacture.client.supplier.RestcallException;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrder;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrderStatus;
import de.novatec.showcase.manufacture.ejb.session.exception.AssemblyNotFoundException;
import de.novatec.showcase.manufacture.ejb.session.exception.InventoryHasNotEnoughPartsException;
import de.novatec.showcase.manufacture.ejb.session.exception.WorkOrderNotFoundException;

public interface WorkOrderService {
	public WorkOrder findWorkOrder(Integer workOrderID) throws WorkOrderNotFoundException;

	public Collection<WorkOrder> getAllWorkOrders();

	public Collection<WorkOrder> getWorkOrderByStatus(WorkOrderStatus status);

	public WorkOrder scheduleWorkOrder(WorkOrder workOrder) throws RestcallException, InventoryHasNotEnoughPartsException, AssemblyNotFoundException;

	public WorkOrder completeWorkOrder(Integer workOrderId, int manufacturedQuantity) throws WorkOrderNotFoundException;

	public WorkOrder cancelWorkOrder(Integer workOrderId) throws WorkOrderNotFoundException, AssemblyNotFoundException;

	public WorkOrder advanceWorkOrderStatus(Integer workOrderId) throws WorkOrderNotFoundException;
}
