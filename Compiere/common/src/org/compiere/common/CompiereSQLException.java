package org.compiere.common;

/**
 * CompiereSQLException should be thrown when catching a SQLException There is a
 * default user message for this exception so there is no need to enter one
 */
public class CompiereSQLException extends RuntimeException {
	private static final long serialVersionUID = 8086185494618121771L;

	/**
	 * The underlying SQLException must be specified. Use the
	 * CompiereSQLException(Throwable) constructor instead.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public CompiereSQLException() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * There is no need to specify a message. Also, the underlying SQLException
	 * must be specified. Use the CompiereSQLException(Throwable) constructor
	 * instead.
	 * 
	 * @param arg0
	 * @throws UnsupportedOperationException
	 */

	public CompiereSQLException(final String arg0)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * There is no need to specify a message. Use the
	 * CompiereSQLException(Throwable) constructor instead.
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws UnsupportedOperationException
	 */
	public CompiereSQLException(final String arg0, final Throwable arg1)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Constructs a CompiereSQLException with a default message
	 * 
	 * @param arg0
	 *            the underlying SQLException
	 */
	public CompiereSQLException(final Throwable arg0) {
		super(arg0);
	}

	/**
	 * Returns the default untranslated message for CompiereSQLException.
	 * 
	 * The top level program is expected to catch this exception and translate
	 * it to a more user friendly message / language.
	 */
	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}

	/**
	 * Returns the default message for CompiereSQLException
	 */
	@Override
	public String getMessage() {
		return "Unexpected SQL Exception - Please check server log for details";
	}
}
