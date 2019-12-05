package de.novatec.showcase.manufacture.ejb.session;

import java.util.Collection;

import de.novatec.showcase.manufacture.ejb.entity.WorkOrder;
import de.novatec.showcase.manufacture.ejb.entity.WorkOrderStatus;

public interface WorkOrderService {
	public WorkOrder findWorkOrder(Integer woId);

	public Collection<WorkOrder> getAllWorkOrders();

	public Collection<WorkOrder> getWorkOrderByStatus(WorkOrderStatus status);

	public int scheduleWorkOrder(WorkOrder wo);

	public void completeWorkOrder(Integer woId, int manufacturedQuantity);

	public void cancelWorkOrder(Integer woId);

	public void advanceWorkOrderStatus(int woId);
}
