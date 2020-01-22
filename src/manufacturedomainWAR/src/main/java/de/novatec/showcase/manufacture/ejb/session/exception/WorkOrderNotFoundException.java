package de.novatec.showcase.manufacture.ejb.session.exception;

public class WorkOrderNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public WorkOrderNotFoundException() {
		super();
	}

	public WorkOrderNotFoundException(String message) {
		super(message);
	}

}
