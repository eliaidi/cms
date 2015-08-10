package com.wk.cms.publish.parser;


import com.wk.cms.parser.HtmlTag;
import com.wk.cms.service.exception.ServiceException;


public interface TagParser {

	String parse(Object obj,Object base, String con) throws ServiceException;
	void setTag(HtmlTag htmlTag);
}
