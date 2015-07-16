package com.wk.cms.service.exception;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8130949617667629706L;

	public ServiceException() {
		super();
	}
	
	public ServiceException(String msg) {
		super(msg);
	}
	
	public ServiceException(String msg,Throwable e) {
		super(msg,e);
	}
}
