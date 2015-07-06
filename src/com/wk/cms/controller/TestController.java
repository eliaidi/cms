package com.wk.cms.controller;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wk.cms.controller.vo.Message;

@Controller
@RequestMapping("/test")
public class TestController {

//	@InitBinder
	public void bind(WebDataBinder binder){
		binder.registerCustomEditor(Map.class, new MapEditorSupport());
	}
	@RequestMapping("")
	public  String t1( M1 m1,@RequestParam(value="a" ,required=false) Integer a){
		
		return "test/t1";
	}
}

class M1{
	private Map<String, Object> m;
	
	public Map<String, Object> getM() {
		return m;
	}
	
	public void setM(Map<String, Object> m) {
		this.m = m;
	}
}

class MapEditorSupport extends PropertyEditorSupport{
	
	@Override
	public String getAsText() {
		// TODO Auto-generated method stub
		return super.getAsText();
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		super.setAsText(text);
	}
}
