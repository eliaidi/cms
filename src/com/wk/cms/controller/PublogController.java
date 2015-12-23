package com.wk.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.service.IPubLogService;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/publog")
public class PublogController {

	@Autowired
	private IPubLogService pubLogService;
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo find(String type,PageInfo pageInfo){
		
		return pubLogService.find(type,pageInfo); 
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Message delete(String ids){
		
		pubLogService.delete(ids);
		return new Message(true, "", null);
	}
}
