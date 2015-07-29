package com.wk.cms.publish.exceptions;

import com.wk.cms.service.exception.ServiceException;

public class PublishException extends ServiceException {

	public PublishException(){}
	public PublishException(String msg) {
		super(msg);
	}

	public PublishException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
