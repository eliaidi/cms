package com.wk.cms.service.aop.validator;

import com.wk.cms.service.exception.ValidationException;

public interface Validator {

	public void validate(Class<?> class1,Object value) throws ValidationException;
}
