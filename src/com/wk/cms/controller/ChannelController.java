package com.wk.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.Channel;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.exception.ServiceException;

@Controller
@RequestMapping("/channel")
public class ChannelController {

	@Autowired
	private IChannelService channelService;
	@RequestMapping("/list")
	public @ResponseBody Map<String,Object> listBySite(String parentId,String siteId) throws ServiceException{
		
		List<Channel> channels;
		if(StringUtils.hasLength(parentId)){
			channels = channelService.findByParentId(parentId);
		}else{
			channels = channelService.findBySiteId(siteId);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("channels", channels);
		return result;
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(Channel channel,String parentId,String siteId) throws ServiceException{
		
		channelService.save(channel,parentId,siteId);
		
		return new Message(true, "保存成功！", channel);
	}
	
	@RequestMapping("/detail")
	public @ResponseBody Message detail(String channelId) throws ServiceException{
		
		Channel channel = channelService.findById(channelId);
		return new Message(true, "", channel);
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String channelId) throws ServiceException{
		
		channelService.deleteById(channelId);
		return new Message(true, "删除成功！", null);
	}
}
