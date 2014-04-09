package org.compiere.common;

public class CompiereStateException extends IllegalStateException {
	private static final long serialVersionUID = 8086185494618121771L;

	public CompiereStateException() {
		super();
	}

	public CompiereStateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CompiereStateException(String arg0) {
		super(arg0);
	}

	public CompiereStateException(Throwable arg0) {
		super(arg0);
	}

}
