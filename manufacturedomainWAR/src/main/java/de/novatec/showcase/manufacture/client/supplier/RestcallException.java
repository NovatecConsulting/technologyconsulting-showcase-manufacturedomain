package de.novatec.showcase.manufacture.client.supplier;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class RestcallException extends Exception {

	private static final long serialVersionUID = -2605722900558101557L;

	public RestcallException() {
		super();
	}

	public RestcallException(String message) {
		super(message);
	}

}
