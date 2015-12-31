package com.wk.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.Resource;
import com.wk.cms.service.IResourceService;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/resource")
public class ResourceController {

	@Autowired
	private IResourceService resourceService;
	
	@RequestMapping("/list")
	public @ResponseBody PageInfo list(PageInfo info,String query){
		return resourceService.list(info,query);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(Resource resource){
		return new Message(true, "保存成功！", resourceService.save(resource));
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String[] ids){
		resourceService.delete(ids);
		return new Message(true, "删除成功！", "");
	}
}
