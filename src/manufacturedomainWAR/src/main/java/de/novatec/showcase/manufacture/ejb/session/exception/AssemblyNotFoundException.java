package de.novatec.showcase.manufacture.ejb.session.exception;

public class AssemblyNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AssemblyNotFoundException() {
		super();
	}

	public AssemblyNotFoundException(String message) {
		super(message);
	}

}
