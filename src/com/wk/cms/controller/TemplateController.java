package com.wk.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.Template;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/template")
public class TemplateController {
	

	@Autowired
	private ITemplateService templateService;

	@RequestMapping("/list")
	public @ResponseBody
	PageInfo list(PageInfo pageInfo, String query) {

		return templateService.find(pageInfo, query);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(Template template){
		
		templateService.save(template);
		return new Message(true, "保存成功！！", template);
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(@RequestParam("ids") String[] ids){
		
		templateService.delete(ids);
		return new Message(true, "删除成功！！", null);
	}
}
