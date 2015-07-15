package com.wk.cms.service.aop.validator;

import com.wk.cms.service.exception.ValidationException;

public class NotEmptyValidator implements Validator {

	@Override
	public void validate(Class<?> class1, Object value)
			throws ValidationException {
		if(value==null){
			throw new ValidationException("参数错误，字段值为空！");
		}
		
		if(class1.equals(String.class)){
			if("".equals(value)){
				throw new ValidationException("参数错误，字段值为空字符串！");
			}
		}else if(class1.isAssignableFrom(Object[].class)){
			Object[] objects = (Object[]) value;
			if(objects.length==0)
				throw new ValidationException("参数错误，数组为空！");
		}
	}

}
