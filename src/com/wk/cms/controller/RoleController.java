package com.wk.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.Role;
import com.wk.cms.service.IRoleService;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private IRoleService roleService;
	
	@RequestMapping("/list")
	public @ResponseBody PageInfo list(PageInfo info,String query,String userId){
		
		return roleService.list(info,query,userId);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message add(Role role){
		
		return new Message(true, "保存成功!", roleService.save(role));
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String[] ids){
		
		roleService.delete(ids);
		return new Message(true, "删除成功!", "");
	}
	
}
