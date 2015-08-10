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
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.publish.server.PublishServer;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PublishUtils;

@Component("ChannelsComp")
public class ChannelsParser extends AbstractTagParser {

	@Autowired
	private IChannelService channelService;
	@Override
	protected String parseInternal(Object obj,Object base, String con)
			throws ServiceException {
		
		String name = e.attr("name");
		String num = e.attr("num");
		String order = e.attr("order");
		String where = e.attr("where");
		String startpos = e.attr("startpos");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("num", num);
		params.put("order", order);
		params.put("where", where);
		params.put("startpos", startpos);
		
		List<Channel> channels = null;
		Site currSite = PublishUtils.getSite(obj);
		
		if(StringUtils.hasLength(name)){
			Channel pChannel = channelService.findByName(name,currSite);
			channels = channelService.findByMap(pChannel,params);
		}else{
			if(obj instanceof Site){
				channels = channelService.findByMap((Site)obj,params);
			}else{
				if(obj instanceof Channel){
					channels = channelService.findByMap((Channel)obj,params);
				}else if(obj instanceof Document){
					Channel currChnl = ((Document)obj).getChannel();
					channels = channelService.findByMap(currChnl,params);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		if(channels!=null){
			for(Channel c : channels){
				sb.append(PublishServer.parse(c,base, e.getHtml()));
			}
		}
		
		return sb.toString();
	}

}
