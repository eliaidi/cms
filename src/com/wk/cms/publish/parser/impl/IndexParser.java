package com.wk.cms.publish.parser.impl;

import org.springframework.stereotype.Component;

import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.service.exception.ServiceException;

@Component("IndexComp")
public class IndexParser extends AbstractTagParser {

	@Override
	protected String parseInternal(Object obj, Object base, String con)
			throws ServiceException {
		// needn't implement this method,it has been done in super class,and never come into this method
		return null;
	}

}
