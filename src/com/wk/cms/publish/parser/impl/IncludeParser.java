package com.wk.cms.publish.parser.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wk.cms.model.Template;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.service.exception.ServiceException;

@Component("IncludeComp")
public class IncludeParser extends AbstractTagParser {

	@Autowired
	private ITemplateService templateService;
	@Override
	protected String parseInternal(Object obj, String con) throws ServiceException {
		String tName = e.attr("w_name");
		String iType = e.attr("w_type");
		
		Template t = templateService.findByName(tName);
		Document doc = Jsoup.parse(t.getConInStr());
		
		return "head".equalsIgnoreCase(iType)?doc.head().html():doc.body().html();
	}

}
