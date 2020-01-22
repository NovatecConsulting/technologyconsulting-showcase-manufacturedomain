package de.novatec.showcase.manufacture.ejb.session.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class InventoryHasNotEnoughPartsException extends Exception {

	private static final long serialVersionUID = 1L;

	public InventoryHasNotEnoughPartsException() {
		super();
	}

	public InventoryHasNotEnoughPartsException(String message) {
		super(message);
	}

}
