package com.wk.cms.mvc.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.validation.constraints.NotNull;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import com.wk.cms.service.aop.validator.NotEmptyValidator;
import com.wk.cms.service.aop.validator.NotNullValidator;
import com.wk.cms.service.aop.validator.Validator;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;

@Component
@Aspect
public class ValidationAspect {

	@Pointcut("execution( * com.wk.cms.service..*.*(..))")
	private void t() {
	}

	@Before("t()")
	public void validate(JoinPoint jp) throws ServiceException {

		MethodSignature methodSignature = (MethodSignature) jp.getSignature();
		Method method = methodSignature.getMethod();

		Annotation[][] annotations = method.getParameterAnnotations();
		Object[] args = jp.getArgs();
		Class<?>[] types = (Class<?>[]) method.getParameterTypes();
		if (annotations != null && annotations.length > 0) {
			for (int i = 0; i < annotations.length; i++) {
				Annotation[] annos = annotations[i];
				if (CommonUtils.isEmpty(annos)) {
					continue;
				}

				for (Annotation anno : annos) {
					Validator validator = getValidator(anno);
					if (validator != null) {
						validator.validate(types[i], args[i]);
					}
				}
			}
		}
	}

	private Validator getValidator(Annotation anno) {

		Class<?>[] interfaces = anno.getClass().getInterfaces();
		if (anno.getClass().equals(NotEmpty.class)
				|| CommonUtils.contains(interfaces, NotEmpty.class)) {
			return new NotEmptyValidator();
		} else if (anno.getClass().equals(NotNull.class)
				|| CommonUtils.contains(interfaces, NotNull.class)) {
			return new NotNullValidator();
		}
		return null;
	}

}
