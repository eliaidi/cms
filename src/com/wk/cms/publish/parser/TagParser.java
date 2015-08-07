package com.wk.cms.publish.parser;

import org.jsoup.nodes.Element;

import com.wk.cms.service.exception.ServiceException;


public interface TagParser {

	void setElement(Element e);
	String parse(Object obj,String con) throws ServiceException;
}
