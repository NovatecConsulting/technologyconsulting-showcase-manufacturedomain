package de.novatec.showcase.manufacture.ejb.session.exception;

public class BomNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public BomNotFoundException() {
		super();
	}

	public BomNotFoundException(String message) {
		super(message);
	}

}
