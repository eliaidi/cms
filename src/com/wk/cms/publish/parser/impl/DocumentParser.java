package com.wk.cms.publish.parser.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Document;
import com.wk.cms.model.Template;
import com.wk.cms.model.Template.Type;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;

@Component("DocumentComp")
public class DocumentParser extends AbstractTagParser {

	@Autowired
	private IDocumentService documentService;
	
	@Autowired
	private IChannelService channelService;

	@Override
	protected String parseInternal(Object obj)
			throws ServiceException {

		HtmlTag e = getTag();
		
		String id = e.attr("id");// id of the document
		String field = e.attr("field");// default:title
		String num = e.attr("num");// number of the results
		String type = e.attr("type");// HTML、TEXT
		String format = e.attr("format");// HTML、JS
		String dateFormat = e.attr("dateformat");// etc:yyyy-MM-dd HH:mm:ss
		String index = e.attr("index"); // extfield's value index
		Document currDoc = null;
		if (StringUtils.hasLength(id)) {
			currDoc = documentService.findById(id);
		} else {
			if (obj instanceof Document) {
				currDoc = (Document) obj;
			} else {
				throw new ServiceException("错误的上下文环境！" + obj);
			}
		}
		if (!StringUtils.hasLength(field)) {
			field = "title";
		}

		Object val = documentService.getDocProperty(currDoc ,field,StringUtils.hasLength(index)?Integer.parseInt(index):null);
		String valStr = null;
		if ((val instanceof Date)) {
			if (!StringUtils.hasLength(dateFormat)) {
				dateFormat = "yyyy-MM-dd HH:mm:ss";
			}
			valStr = new SimpleDateFormat(dateFormat).format(val);
		}else if(val instanceof List){
			valStr = CommonUtils.join((List)val, ",");
		} else {
			valStr = val.toString();
			if ("text".equalsIgnoreCase(type)) {
				valStr = valStr.replaceAll("<script[^>]*>(.*?)</script>", "")
						.replaceAll("<style[^>]*>(.*?)</style>", "")
						.replaceAll("<[^>]+>", "");
			}
			if("html".equalsIgnoreCase(format)){
				valStr = StringEscapeUtils.escapeHtml4(valStr);
			}else if("js".equalsIgnoreCase(format)){
				valStr = StringEscapeUtils.escapeEcmaScript(valStr);
			}
			
			if(StringUtils.hasLength(num)){
				int n = Integer.parseInt(num);
				valStr = valStr.length()>n?valStr.substring(0, n):valStr;
			}
		}
		return valStr;
	}

	@Override
	protected String getPubFileName(Object obj) {
		
		Document doc = (Document) obj;
		List<Template> templates = channelService.findTemps(((Document)obj).getChannel(),Type.DETAIL);
		if(CommonUtils.isEmpty(templates)){
			return "document-"+doc.getId()+".html";
		}
		Template template = templates.get(0);
		return template.getPrefix()+"-"+doc.getId()+".html";
	}
}
