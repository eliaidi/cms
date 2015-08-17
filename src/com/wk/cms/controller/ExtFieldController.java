package com.wk.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.ExtField;
import com.wk.cms.service.IExtFieldService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/extfield")
public class ExtFieldController {

	@Autowired
	private IExtFieldService extFieldService;
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo list(String channelId,PageInfo pageInfo,String query){
		
		return extFieldService.find(channelId,pageInfo,query);
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Message save(ExtField extField) throws ServiceException{
		
		return new Message(true, "保存成功！", extFieldService.save(extField));
	}
}
