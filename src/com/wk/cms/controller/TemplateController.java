package com.wk.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
