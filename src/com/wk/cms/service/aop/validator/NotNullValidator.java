package com.wk.cms.service.aop.validator;

import com.wk.cms.service.exception.ValidationException;

public class NotNullValidator implements Validator {

	@Override
	public void validate(Class<?> class1, Object value)
			throws ValidationException {
		if(value==null){
			throw new ValidationException("参数错误，字段值为空！");
		}

	}

}
