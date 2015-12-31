package com.wk.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.User;
import com.wk.cms.service.IUserService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;
	@RequestMapping("/list")
	public @ResponseBody PageInfo list(PageInfo info,String query){
		
		return userService.list(info,query);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(User user) throws ServiceException{
		
		return new Message(true, "保存成功!", userService.save(user));
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String[] id) throws ServiceException{
		
		userService.delete(id);
		return new Message(true, "删除成功!", "");
	}
	
	@RequestMapping("/assign")
	public @ResponseBody Message assign(String userId,String[] roleIds) throws ServiceException{
		
		userService.assign(userId,roleIds);
		return new Message(true, "操作成功!", "");
	}
}
