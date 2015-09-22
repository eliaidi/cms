package com.wk.cms.publish.parser;

import com.wk.cms.publish.server.PublishContext;
import com.wk.cms.service.exception.ServiceException;

public interface TagParser {

	String parse() throws ServiceException;

	void setContext(PublishContext ctx);
}
