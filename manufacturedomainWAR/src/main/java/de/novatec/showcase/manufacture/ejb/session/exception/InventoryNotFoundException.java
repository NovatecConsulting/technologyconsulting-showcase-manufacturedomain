package de.novatec.showcase.manufacture.ejb.session.exception;

public class InventoryNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public InventoryNotFoundException() {
		super();
	}

	public InventoryNotFoundException(String message) {
		super(message);
	}

}
