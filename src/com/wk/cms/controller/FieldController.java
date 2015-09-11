package com.wk.cms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.Field;
import com.wk.cms.service.IFieldService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/field")
public class FieldController {

	@Autowired
	private IFieldService fieldService;

	@RequestMapping("/list")
	@ResponseBody
	public PageInfo list(String siteId, PageInfo pageInfo, String query) {

		return fieldService.find(siteId, pageInfo, query);
	}

	@RequestMapping("/listTypes")
	@ResponseBody
	public Map<String, Object> listTypes(String siteId) throws ServiceException {
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("types", fieldService.findTypes(siteId));
		return r;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Message save(Field field) throws ServiceException {
		return new Message(true, "保存成功！", fieldService.save(field));
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Message delete(String[] ids) throws ServiceException {
		fieldService.delete(ids);
		return new Message(true, "删除成功！", null);
	}
	
	@RequestMapping("/detail")
	@ResponseBody
	public Message detail(String id) throws ServiceException {
		return new Message(true, "操作成功！", fieldService.findById(id));
	}
}
