package com.wk.cms.web.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.wk.cms.log.SpringLogBackFactory;

public class SpringLogBackListener implements ServletContextListener{

	private static final String LOGFACTORY_KEY = "org.apache.commons.logging.LogFactory";
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.setProperty(LOGFACTORY_KEY, SpringLogBackFactory.class.getName());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//System.setProperty(LOGFACTORY_KEY, null);
	}

}

