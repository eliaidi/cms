package com.wk.cms.service.exception;

public class FileParseException extends ServiceException {

	public FileParseException(){
		
	}
	public FileParseException(String string) {
		super(string);
	}

	public FileParseException(String string, Throwable e) {
		super(string, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
