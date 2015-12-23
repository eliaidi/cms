package com.wk.cms.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login(ModelAndView mav,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		Subject subject = SecurityUtils.getSubject();
		if(subject!=null&&subject.isAuthenticated()){
			WebUtils.issueRedirect(request, response, "/");
			return null;
		}
		Object exception = request.getAttribute("shiroLoginFailure");
		if(exception==null){
			//WebUtils.issueRedirect(request, response, "/index");
			//return null;
		}else{
			if(exception instanceof Exception){
				Exception exp = (Exception) exception;
				mav.addObject("errMsg", exp.getMessage());
			}
		}
		
		return "login";
	}
	
}
