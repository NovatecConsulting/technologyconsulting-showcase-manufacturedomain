package de.novatec.showcase.manufacture.ejb.session.exception;

public class ComponentNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ComponentNotFoundException() {
		super();
	}

	public ComponentNotFoundException(String message) {
		super(message);
	}

}
