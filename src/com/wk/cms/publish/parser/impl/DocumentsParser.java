package com.wk.cms.publish.parser.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.publish.exceptions.PublishException;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.publish.server.PublishServer;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.IDocumentService;

@Component("Documents")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocumentsParser extends AbstractTagParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentsParser.class);
	@Autowired
	private IDocumentService documentService;
	
	@Autowired
	private IChannelService channelService;
	@Override
	public String parse(Object obj, String con) {

		Channel currChnl = StringUtils.hasLength(e.attr("id"))?channelService.findById(e.attr("id")):(Channel)obj;
		int pageSize = StringUtils.hasLength(e.attr("num"))?Integer.parseInt("num"):IDocumentService.MAX_FETCH_SIZE;
		String where = StringUtils.hasLength(e.attr("where"))?e.attr("where"):"";
		String order = StringUtils.hasLength(e.attr("order"))?e.attr("order"):"crtime desc";
		List<Document> documents = documentService.findCanPub(currChnl,pageSize,where,order,null);
		
		StringBuffer sb = new StringBuffer();
		for(Document document : documents){
			try {
				sb.append(PublishServer.parse(document, con));
			} catch (PublishException e1) {
				LOGGER.error(e1.getMessage(),e1);
			}
		}
		return sb.toString();
	}

}
