package com.wk.cms.publish.parser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;

@Component("ChannelComp")
public class ChannelParser extends AbstractTagParser {

	@Autowired
	private IChannelService channelService;
	@Override
	protected String parseInternal(Object obj)
			throws ServiceException {
		
		HtmlTag e = getTag();
		String name = e.attr("name");
		Channel channel = null;
		Site currSite = getSite(obj);
		if(StringUtils.hasLength(name)){
			channel = channelService.findByName(name, currSite);
		}else{
			if(obj instanceof Channel){
				channel = (Channel) obj;
			}else if(obj instanceof Document){
				channel = ((Document)obj).getChannel();
			}else{
				throw new ParseException("错误的上下文环境！"+obj);
			}
		}
		
		String field = e.attr("field");
		if(!StringUtils.hasLength(field)){
			field = "descr";
		}
		String value = CommonUtils.getDeepFieldValue(channel, field).toString();
		int showNum = StringUtils.hasLength(e.attr("num"))?Integer.parseInt(e.attr("num")):2000;
		value = value.length()>showNum?value.substring(0, showNum):value;
		
		return value;
	}

	@Override
	protected String getPubFileName(Object obj) {
		return "";
	}
}
