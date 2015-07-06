package com.wk.cms.service.exception;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class ValidationException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BindingResult bindingResult;
	
	public ValidationException(BindingResult result){
		
		this.bindingResult = result;
		
	}
	
	@Override
	public String getMessage() {
		if(bindingResult==null)
			return super.getMessage();
		
		StringBuffer msg = new StringBuffer();
		List<ObjectError> objectErrors = bindingResult.getAllErrors();
		for(ObjectError err : objectErrors){
			msg.append(err.getDefaultMessage()+"<br/>");
		}
		return msg.toString();
	}
}
