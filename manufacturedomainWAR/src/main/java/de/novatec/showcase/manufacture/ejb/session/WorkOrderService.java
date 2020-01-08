package de.novatec.showcase.manufacture.ejb.session;

import java.util.Collection;

import de.novatec.showcase.manufacture.client.supplier.RestcallException;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrder;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrderStatus;

public interface WorkOrderService {
	public WorkOrder findWorkOrder(Integer workOrderID);

	public Collection<WorkOrder> getAllWorkOrders();

	public Collection<WorkOrder> getWorkOrderByStatus(WorkOrderStatus status);

	public int scheduleWorkOrder(WorkOrder workOrder) throws RestcallException;

	public void completeWorkOrder(Integer workOrderId, int manufacturedQuantity);

	public void cancelWorkOrder(Integer workOrderId);

	public void advanceWorkOrderStatus(Integer workOrderId);
}
