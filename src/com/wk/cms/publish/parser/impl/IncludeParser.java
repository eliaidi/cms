package com.wk.cms.publish.parser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wk.cms.model.Template;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;

@Component("IncludeComp")
public class IncludeParser extends AbstractTagParser {

	@Autowired
	private ITemplateService templateService;
	@Override
	protected String parseInternal(Object obj,Object base, String con) throws ServiceException {
		String tName = e.attr("name");
		
		Template t = templateService.findByName(tName);
		try {
			return CommonUtils.getContent(t.getFile().getContent().getBinaryStream());
		} catch (Exception e1) {
			throw new ServiceException("获取模板正文失败！",e1);
		} 
	}

}
