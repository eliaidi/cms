package com.wk.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String index(){
		return "main";
	}
	
	@RequestMapping("/test")
	public String test(){
		return "test";
	}
}
