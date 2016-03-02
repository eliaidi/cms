package com.wk.cms.utils;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class BeanFactory implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		BeanFactory.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class<T> clazz){
		
		return applicationContext.getBean(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		/*String[] names = applicationContext.getBeanDefinitionNames();
		for(String n : names){
			System.err.println(n+"----"+applicationContext.getBean(n));
		}*/
//		System.err.println(applicationContext.getBean(HandlerMapping.class));
		/*SimpleUrlHandlerMapping mapping = (SimpleUrlHandlerMapping) applicationContext.getBean(HandlerMapping.class);
		Map<String, ?> m =  mapping.getUrlMap();
		for(String k : m.keySet()){
			System.err.println(k+"----"+m.get(k));
		}*/
		return (T) applicationContext.getBean(beanName);
	}
	
}
