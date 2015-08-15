package com.wk.cms.publish.parser.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.publish.server.PublishServer;
import com.wk.cms.publish.vo.PubObj;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.exception.ServiceException;

@Component("DocumentsComp")
public class DocumentsParser extends AbstractTagParser {

	@Autowired
	private IDocumentService documentService;
	
	@Autowired
	private IChannelService channelService;
	@Override
	protected String parseInternal(Object obj)
			throws ServiceException {
		
		HtmlTag e = getTag();
		Object base = ctx.getBase();
		String name = e.attr("name");
		String num = e.attr("num");
		String order = e.attr("order");
		String where = e.attr("where");
		String startpos = e.attr("startpos");
		
		Channel currChnl = null;
		Site currSite = getSite(obj);
		if(StringUtils.hasLength(name)){
			currChnl = channelService.findByName(name, currSite);
		}else{
			if(obj instanceof Channel){
				currChnl = (Channel) obj;
			}else if(obj instanceof Document){
				currChnl = ((Document) obj).getChannel();
			}else{
				throw new ServiceException("错误的上下文环境！"+obj);
			}
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("num", num);
		params.put("order", order);
		params.put("where", where);
		params.put("startpos", startpos);
		
		List<Document> documents = documentService.findByMap(currChnl,params);
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<documents.size();i++){
			Document doc = documents.get(i);
			sb.append(PublishServer.parse(new PubObj((i+1),doc), base, e.getHtml()));
		}
		return sb.toString();
	}

}
