package com.wk.cms.exception;

public class ParseException extends Exception {

	public ParseException(String string, Throwable e) {
		super(string, e);
	}

	public ParseException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
