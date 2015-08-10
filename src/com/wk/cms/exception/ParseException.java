package com.wk.cms.exception;

import com.wk.cms.service.exception.ServiceException;

public class ParseException extends ServiceException {

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
